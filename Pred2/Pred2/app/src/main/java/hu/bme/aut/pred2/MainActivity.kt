package hu.bme.aut.pred2

import android.R.attr
import android.os.Bundle
import android.view.View
import hu.bme.aut.pred2.R.id.button
import org.tensorflow.lite.Interpreter
import androidx.fragment.app.activityViewModels
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.R.attr.value
import android.annotation.SuppressLint

import android.content.Intent
import android.provider.SyncStateContract.Helpers.update
import android.system.Os.bind
import android.system.Os.close
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.pred2.R.id.drawerLayout
import hu.bme.aut.pred2.adapter.TeamAdapter
import hu.bme.aut.pred2.data.Match
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.data.TeamDatabase
import hu.bme.aut.pred2.databinding.ActivityMainBinding
import hu.bme.aut.pred2.databinding.ActivityTeamDetailsBinding
import kotlinx.coroutines.flow.collect
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

//////
private val MODEL_ASSETS_PATH = "dfwinnerpredict_4.tflite"
private var tflite : Interpreter? = null
///////

class MainActivity : AppCompatActivity() {

    private lateinit var match: Match
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: TeamDatabase
    private var items = mutableListOf<Team>()
    private lateinit var hometeam : Team
    private lateinit var awayteam : Team
    private lateinit var hometeamiv : ImageView
    private lateinit var awayteamiv : ImageView

    lateinit var toggle : ActionBarDrawerToggle

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = TeamDatabase.getDatabase(applicationContext)
        initItems()
        Log.i("MainActivity",items.size.toString())


        //https://www.youtube.com/watch?v=do4vb0MdLFY&ab_channel=PhilippLackner
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.Open, R.string.Close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.predictor_nav -> Toast.makeText(applicationContext,"Alright", Toast.LENGTH_SHORT)
                R.id.team_nav -> {
                    val intent = Intent(this, TeamActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        //https://stackoverflow.com/questions/6092093/how-to-put-an-app-main-thread-to-sleep-to-show-progress-dialog-changes
        //thread altatása ameddig vissza nem érkeznek a csapatok listája
        try {
            Thread.sleep(300)
        } catch (e: InterruptedException) {
            //handle
        }
        Log.i("MainActivity",items.size.toString())
        if(items.size==0){
            firstRun()
            try {
                Thread.sleep(300)
            } catch (e: InterruptedException) {
                //handle
            }
        }
        Log.i("MainActivity",items.size.toString())
        ////////////////////meccselem létrehozás
        for (item in items){
            if(item.teamname == "Villarreal") {
                hometeam = item
            }
            if(item.teamname == "Atl_Madrid"){
                awayteam = item
            }
        }

        val countDateUntil = "2022-01-09 20:00:00"
        bindteamicon(hometeam,0)
        bindteamicon(awayteam,1)
        var input_bethomewinodds: Float = 3.3.toFloat()
        var input_betdrawodds: Float = 3.5.toFloat()
        var input_betguestwinodds: Float = 2.4.toFloat()
        var homeform: Float = 6.toFloat()
        var awayform: Float = 17.toFloat()
        binding.bethometv.setText(input_bethomewinodds.toString())
        binding.betdrawtv.setText(input_betdrawodds.toString())
        binding.betawaytv.setText(input_betguestwinodds.toString())
        hometeamiv = binding.homeiv
        awayteamiv = binding.awayiv
        hometeamiv.setOnClickListener{
            //holder.binding.tvName.text = "Szia"
            val intent = Intent(this, TeamDetailsActivity::class.java)
            intent.putExtra("team", hometeam)
            startActivity(intent)
        }
        awayteamiv.setOnClickListener{
            //holder.binding.tvName.text = "Szia"
            val intent = Intent(this, TeamDetailsActivity::class.java)
            intent.putExtra("team", awayteam)
            startActivity(intent)
        }
        /////////////////////////////////////
        // Kiértékeléshez szükséges attribútumok
        // 'OverallRatingDiff','AttackingRatingDiff','MidfieldRatingDiff','DefenceRatingDiff','AverageAgeDiff','DefenceWidthDiff','DefenceDepthDiff','OffenceWidthDiff','bethomewinodds','betdrawodds','betguestwinodds'
        //betoddsok meg vannak adva
        var input_OverallRatingDiff : Float = (hometeam.overall - awayteam.overall).toFloat()
        var input_AttackingRatingDiff : Float = (hometeam.attackingRating - awayteam.attackingRating).toFloat()
        var input_MidfieldRatingDiff : Float = (hometeam.midfieldRating - awayteam.midfieldRating).toFloat()
        var input_DefenceRatingDiff : Float = (hometeam.defenceRating - awayteam.defenceRating).toFloat()
        var input_AverageAgeDiff : Float = (hometeam.xIAverageAge - awayteam.xIAverageAge).toFloat()
        var input_DefenceWidthDiff : Float = (hometeam.defenceWidth - awayteam.defenceWidth).toFloat()
        var input_DefenceDepthDiff : Float = (hometeam.defenceDepth - awayteam.defenceDepth).toFloat()
        var input_OffenceWidthDiff : Float = (hometeam.offenceWidth - awayteam.offenceWidth).toFloat()
        var input_FormDiff: Float = homeform - awayform
        /////////////////////Visszaszámoló
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val now = Date()
        try {
            val date = sdf.parse(countDateUntil)
            val currentTime = now.time
            val christmasDate = date.time
            val countDownToChristmas = christmasDate - currentTime
            binding.cCounter.start(countDownToChristmas)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        fun countDate(d: String): Boolean{
            val dateDay = sdf.parse(d)
            val dayDate = dateDay.time
            val currentTime = now.time
            val countDownToDay = dayDate - currentTime
            val end = 0
            when {
                (countDownToDay <= end.toLong()) -> {
                    return true
                }
            }
            return false
        }
        //////////////////////////////////////////

        //////////////////////////Modell implementálás
//https://github.com/shubham0204/Spam_Classification_Android_Demo/blob/master/app/src/main/java/com/ml/quaterion/spamo/Classifier.kt
//https://www.youtube.com/watch?v=RhjBDxpAOIc&ab_channel=TensorFlow
        tflite = Interpreter( loadModelFile() )
        var button = binding.button
        var Hometv = binding.HometextView
        var Drawtv = binding.DrawtextView
        var Awaytv  = binding.AwaytextView

        button.setOnClickListener(View.OnClickListener {
            var inputs: Array<Float> = arrayOf(
                input_OverallRatingDiff,
                input_AttackingRatingDiff,
                input_MidfieldRatingDiff,
                input_DefenceRatingDiff,
                input_AverageAgeDiff,
                input_DefenceWidthDiff,
                input_DefenceDepthDiff,
                input_OffenceWidthDiff,
                input_FormDiff,
                input_bethomewinodds,
                input_betdrawodds,
                input_betguestwinodds,
            )
            var results = classifySequence(inputs)
            var class1 = results[0]
            var class2 = results[1]
            var class3 = results[2]
            var corrres1 = 1/class1
            var corrres3 = 1/class2
            var corrres2 = 1/class3
            Hometv.setText(corrres1.toString())
            Drawtv.setText(corrres3.toString())
            Awaytv.setText(corrres2.toString())
        })
        ////////////////////////////////////////
    }
    //////////////////csapatadatok betöltése
    private fun initItems() {
        thread {
            var itemlist = database.teamDao().getAll()
            items = itemlist.toMutableList()
        }
    }

    //TODO https://github.com/tensorflow/tensorflow/issues/31688
    //elméletileg a tensorflow lite nem támogatja a sigmoid eljárást csak a relu-t
    //https://www.tensorflow.org/lite/guide/ops_compatibility
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val assetFileDescriptor = assets.openFd(MODEL_ASSETS_PATH)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun classifySequence (sequence: Array<Float>): FloatArray {
        val inputs : Array<FloatArray> = arrayOf( sequence.map { it.toFloat() }.toFloatArray())
        var outputs : Array<FloatArray> = arrayOf( FloatArray( 3 ) )
        tflite?.run( inputs , outputs )
        return outputs[0]
    }
    private fun getMatchItem(_date: Date, _homeTeam: Team, _awayTeam: Team, _bethomeodds: Float, _betdrawodds: Float, _betawayodds: Float)= Match(
        date = _date,
        homeTeam = _homeTeam,
        awayTeam = _awayTeam,
        bethomeodds = _bethomeodds,
        betdrawodds = _betdrawodds,
        betawayodds = _betawayodds
    )

    private fun bindteamicon(team: Team, i: Int){
        var img: ImageView
        if(i ==0){
            img = binding.homeiv
        }
        else{
            img = binding.awayiv
        }

        when (team.field1) {
            0 ->img.setImageResource(R.drawable.alaves)
            1 ->img.setImageResource(R.drawable.almeria)
            2 ->img.setImageResource(R.drawable.ath_bilbao)
            3 ->img.setImageResource(R.drawable.atl_madrid)
            4 ->img.setImageResource(R.drawable.barcelona)
            5 ->img.setImageResource(R.drawable.betis)
            6 ->img.setImageResource(R.drawable.cadiz)
            7 ->img.setImageResource(R.drawable.celta_vigo)
            8 ->img.setImageResource(R.drawable.cordoba)
            9 ->img.setImageResource(R.drawable.deportivo)
            10 ->img.setImageResource(R.drawable.eibar)
            11 ->img.setImageResource(R.drawable.elche)
            12 ->img.setImageResource(R.drawable.espanyol)
            13 ->img.setImageResource(R.drawable.getafe)
            14 ->img.setImageResource(R.drawable.gijon)
            15 ->img.setImageResource(R.drawable.girona)
            16 ->img.setImageResource(R.drawable.granada)
            17 ->img.setImageResource(R.drawable.huesca)
            18 ->img.setImageResource(R.drawable.las_palmas)
            19 ->img.setImageResource(R.drawable.leganes)
            20 ->img.setImageResource(R.drawable.levante)
            21 ->img.setImageResource(R.drawable.malaga)
            22 ->img.setImageResource(R.drawable.mallorca)
            23 ->img.setImageResource(R.drawable.osasuna)
            24 ->img.setImageResource(R.drawable.rayo_vallecano)
            25 ->img.setImageResource(R.drawable.real_madrid)
            26 ->img.setImageResource(R.drawable.real_sociedad)
            27 ->img.setImageResource(R.drawable.sevilla)
            28 ->img.setImageResource(R.drawable.valencia)
            29 ->img.setImageResource(R.drawable.valladolid)
            30 ->img.setImageResource(R.drawable.villarreal)
            else -> img.setImageResource(R.drawable.barcelona)
        }
    }

    //menu miatt
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onTeamCreated(newItem: Team) {
        thread {
            val insertId = database.teamDao().insert(newItem)
        }
    }

    private fun getTeamItem(field1_p: Int, teamname_p: String, matches_played_p: Int, overall_p: Double, attackingRating_p: Double, midfieldRating_p: Double, defenceRating_p: Double, clubWorth_p: Double, xIAverageAge_p: Double, defenceWidth_p: Double, defenceDepth_p: Double, offenceWidth_p: Double, likes_p: Double, dislikes_p: Double, avgoals_p: Double, avconceded_p: Double, avgoalattempts_p: Double, avshotsongoal_p: Double, avshotsoffgoal_p: Double, avblockedshots_p: Double, avpossession_p: Double, avfreekicks_p: Double, avGoalDiff_p: Double, avwins_p: Double, avdraws_p: Double, avloses_p: Double) = Team(
        field1 = field1_p,
        teamname = teamname_p,
        matches_played = matches_played_p,
        overall = overall_p.toInt(),
        attackingRating = attackingRating_p.toFloat(),
        midfieldRating = midfieldRating_p.toFloat(),
        defenceRating = defenceRating_p.toFloat(),
        clubWorth = clubWorth_p.toFloat(),
        xIAverageAge = xIAverageAge_p.toFloat(),
        defenceWidth = defenceWidth_p.toFloat(),
        defenceDepth = defenceDepth_p.toFloat(),
        offenceWidth = offenceWidth_p.toFloat(),
        likes = likes_p.toFloat(),
        dislikes = dislikes_p.toFloat(),
        avgoals = avgoals_p.toFloat(),
        avconceded = avconceded_p.toFloat(),
        avgoalattempts = avgoalattempts_p.toFloat(),
        avshotsongoal = avshotsongoal_p.toFloat(),
        avshotsoffgoal = avshotsoffgoal_p.toFloat(),
        avblockedshots = avblockedshots_p.toFloat(),
        avpossession = avpossession_p.toFloat(),
        avfreekicks = avfreekicks_p.toFloat(),
        avGoalDiff = avGoalDiff_p.toFloat(),
        avwins = avwins_p.toFloat(),
        avdraws = avdraws_p.toFloat(),
        avloses = avloses_p.toFloat()
    )

    private fun firstRun(){
        Log.i("MainActivity","töltés")
        onTeamCreated(getTeamItem(0,"Alaves",190,74.0,75.0,73.0,75.0,13.0,28.64,50.0,30.0,70.0,45.0,15.0,1.0,1.36315789473684,9.83157894736842,3.14210526315789,4.52631578947368,2.16315789473684,42.0736842105263,15.5263157894737,0.289473684210526,0.321052631578947,0.242105263157895,0.436842105263158))
        onTeamCreated(getTeamItem(1,"Almeria",76,72.0,73.0,70.0,71.0,150.0,25.18,45.0,37.0,54.0,32.0,5.0,1.02631578947368,1.77631578947368,10.4078947368421,3.47368421052632,4.55263157894737,2.38157894736842,43.8684210526316,15.2631578947368,0.407894736842105,0.25,0.197368421052632,0.552631578947369))
        onTeamCreated(getTeamItem(2,"Ath_Bilbao",304,78.0,80.0,79.0,78.0,250.0,27.64,50.0,50.0,70.0,148.0,31.0,1.27631578947368,1.125,11.5986842105263,4.125,5.03947368421053,2.43421052631579,50.7894736842105,16.3519736842105,0.473684210526316,0.391447368421053,0.282894736842105,0.325657894736842))
        onTeamCreated(getTeamItem(3,"Atl_Madrid",303,84.0,84.0,84.0,83.0,1200.0,27.91,60.0,60.0,40.0,400.0,113.0,1.67656765676568,0.66996699669967,12.1353135313531,4.61716171617162,4.95379537953795,2.56435643564356,49.0462046204621,13.8910891089109,0.399339933993399,0.63036303630363,0.234323432343234,0.135313531353135))
        onTeamCreated(getTeamItem(4,"Barcelona",304,83.0,81.0,84.0,80.0,3200.0,25.09,60.0,80.0,30.0,1449.0,541.0,2.625,0.858552631578947,15.6052631578947,6.79605263157895,5.48026315789474,3.32894736842105,66.2269736842105,16.8421052631579,0.457236842105263,0.713815789473684,0.171052631578947,0.115131578947368))
        onTeamCreated(getTeamItem(5,"Betis",266,79.0,79.0,80.0,77.0,175.0,30.27,60.0,70.0,60.0,165.0,49.0,1.17669172932331,1.56766917293233,11.6992481203008,4.10526315789474,4.67669172932331,2.91729323308271,53.4285714285714,17.6578947368421,0.360902255639098,0.323308270676692,0.236842105263158,0.43984962406015))
        onTeamCreated(getTeamItem(6,"Cadiz_CF",38,74.0,76.0,74.0,73.0,3.5,27.73,70.0,50.0,80.0,31.0,8.0,0.947368421052632,1.52631578947368,8.0,2.68421052631579,4.15789473684211,1.15789473684211,34.2368421052632,17.4210526315789,-0.105263157894737,0.289473684210526,0.289473684210526,0.421052631578947))
        onTeamCreated(getTeamItem(7,"Celta_Vigo",304,76.0,80.0,76.0,75.0,110.0,28.45,70.0,60.0,40.0,62.0,11.0,1.32894736842105,1.49342105263158,11.3684210526316,4.10855263157895,4.5296052631579,2.73026315789474,54.0427631578947,15.608552631579,0.355263157894737,0.332236842105263,0.269736842105263,0.398026315789474))
        onTeamCreated(getTeamItem(8,"Cordoba",38,71.0,72.0,71.0,70.0,12.6,25.36,52.0,38.0,59.0,9.0,0.0,0.578947368421053,1.78947368421053,10.3947368421053,3.21052631578947,5.0,2.18421052631579,46.3684210526316,16.3157894736842,0.105263157894737,0.0789473684210526,0.289473684210526,0.631578947368421))
        onTeamCreated(getTeamItem(9,"Dep_La_Coruna",152,77.0,79.0,77.0,77.0,100.0,29.09,66.0,47.0,78.0,61.0,11.0,1.05921052631579,1.69736842105263,11.6644736842105,3.75657894736842,5.25,2.65789473684211,47.1907894736842,15.5328947368421,0.322368421052632,0.190789473684211,0.361842105263158,0.447368421052632))
        onTeamCreated(getTeamItem(10,"Eibar",266,76.0,76.0,75.0,74.0,29.0,28.55,50.0,80.0,60.0,34.0,8.0,1.11654135338346,1.40977443609023,11.687969924812,3.87593984962406,5.19924812030075,2.61278195488722,48.2894736842105,14.4624060150376,0.37593984962406,0.289473684210526,0.266917293233083,0.443609022556391))
        onTeamCreated(getTeamItem(11,"Elche",114,75.0,76.0,75.0,73.0,4.8,29.0,50.0,50.0,50.0,23.0,3.0,0.868421052631579,1.46491228070175,9.47368421052632,3.00877192982456,4.47368421052632,1.99122807017544,47.5438596491228,16.1578947368421,0.315789473684211,0.245614035087719,0.289473684210526,0.464912280701754))
        onTeamCreated(getTeamItem(12,"Espanyol",266,77.0,78.0,77.0,76.0,60.0,28.0,60.0,50.0,60.0,75.0,28.0,1.08270676691729,1.41353383458647,10.609022556391,3.65037593984962,4.50751879699248,2.45112781954887,46.1541353383459,15.8533834586466,0.308270676691729,0.308270676691729,0.266917293233083,0.424812030075188))
        onTeamCreated(getTeamItem(13,"Getafe",266,76.0,75.0,77.0,76.0,45.0,26.18,40.0,70.0,70.0,50.0,21.0,1.0,1.25187969924812,10.6278195488722,3.46616541353383,4.62781954887218,2.53383458646617,43.3458646616541,16.5601503759398,0.462406015037594,0.31203007518797,0.270676691729323,0.417293233082707))
        onTeamCreated(getTeamItem(14,"Gijon",76,74.0,75.0,73.0,73.0,17.0,25.45,41.0,45.0,43.0,24.0,6.0,1.07894736842105,1.76315789473684,9.80263157894737,3.38157894736842,4.05263157894737,2.36842105263158,43.7631578947368,14.5,0.368421052631579,0.223684210526316,0.25,0.526315789473684))
        onTeamCreated(getTeamItem(15,"Girona",76,77.0,79.0,76.0,76.0,9.8,25.91,40.0,50.0,40.0,38.0,8.0,1.14473684210526,1.47368421052632,11.2894736842105,4.01315789473684,4.88157894736842,2.39473684210526,47.4605263157895,16.4078947368421,0.144736842105263,0.302631578947368,0.25,0.447368421052632))
        onTeamCreated(getTeamItem(16,"Granada_CF",227,77.0,77.0,77.0,76.0,55.0,27.09,40.0,50.0,70.0,48.0,10.0,1.03964757709251,1.6784140969163,10.568281938326,3.36563876651982,4.66960352422908,2.53303964757709,44.7488986784141,15.7973568281938,0.480176211453745,0.273127753303965,0.220264317180617,0.506607929515419))
        onTeamCreated(getTeamItem(17,"Huesca",76,73.0,73.0,73.0,73.0,4.5,27.18,60.0,50.0,60.0,22.0,6.0,1.01315789473684,1.55263157894737,11.5921052631579,3.76315789473684,5.27631578947368,2.55263157894737,46.3026315789474,16.1052631578947,0.276315789473684,0.184210526315789,0.328947368421053,0.486842105263158))
        onTeamCreated(getTeamItem(18,"Las_Palmas",114,75.0,77.0,74.0,75.0,38.0,25.82,38.0,47.0,55.0,48.0,8.0,1.07017543859649,1.76315789473684,11.1140350877193,4.14912280701754,4.71052631578947,2.25438596491228,56.1929824561403,19.4385964912281,0.570175438596491,0.236842105263158,0.210526315789474,0.552631578947369))
        onTeamCreated(getTeamItem(19,"Leganes",151,75.0,75.0,75.0,75.0,10.8,27.64,40.0,70.0,70.0,27.0,11.0,0.887417218543046,1.32450331125828,10.9072847682119,3.58940397350993,4.86754966887417,2.45033112582781,43.4437086092715,14.9403973509934,0.344370860927152,0.251655629139073,0.278145695364238,0.470198675496689))
        onTeamCreated(getTeamItem(20,"Levante",266,77.0,79.0,77.0,74.0,49.0,28.27,50.0,60.0,70.0,39.0,13.0,1.13533834586466,1.55639097744361,10.7631578947368,3.49248120300752,4.66541353383459,2.60526315789474,44.9962406015038,15.4511278195489,0.360902255639098,0.278195488721804,0.281954887218045,0.43984962406015))
        onTeamCreated(getTeamItem(21,"Malaga",190,75.0,75.0,75.0,75.0,105.0,26.91,35.0,44.0,47.0,86.0,25.0,1.01052631578947,1.28947368421053,12.0,4.07368421052632,5.28421052631579,2.64210526315789,47.9947368421053,16.3789473684211,0.426315789473684,0.289473684210526,0.231578947368421,0.478947368421053))
        onTeamCreated(getTeamItem(22,"Mallorca",38,74.0,75.0,74.0,73.0,6.8,26.73,50.0,50.0,60.0,49.0,14.0,1.05263157894737,1.71052631578947,11.0526315789474,3.42105263157895,5.05263157894737,2.57894736842105,44.5526315789474,15.921052631579,0.81578947368421,0.236842105263158,0.157894736842105,0.605263157894737))
        onTeamCreated(getTeamItem(23,"Osasuna",152,76.0,78.0,77.0,75.0,15.0,27.82,70.0,70.0,60.0,29.0,2.0,1.01973684210526,1.69736842105263,10.9671052631579,3.63815789473684,4.98684210526316,2.34210526315789,44.0526315789474,14.8223684210526,0.335526315789474,0.25,0.282894736842105,0.467105263157895))
        onTeamCreated(getTeamItem(24,"Rayo_Vallecano",152,74.0,78.0,74.0,72.0,10.0,27.82,60.0,70.0,60.0,40.0,7.0,1.21710526315789,1.91447368421053,13.0592105263158,4.46052631578947,5.54605263157895,3.05263157894737,55.2105263157895,16.9671052631579,0.5,0.296052631578947,0.177631578947368,0.526315789473684))
        onTeamCreated(getTeamItem(25,"Real_Madrid",304,84.0,83.0,86.0,83.0,3100.0,28.09,50.0,80.0,50.0,959.0,451.0,2.40789473684211,0.967105263157895,17.1184210526316,6.73026315789474,6.79934210526316,3.58881578947368,58.6611842105263,15.1940789473684,0.388157894736842,0.68421052631579,0.174342105263158,0.141447368421053))
        onTeamCreated(getTeamItem(26,"Real_Sociedad",304,80.0,82.0,80.0,77.0,150.0,25.18,40.0,50.0,60.0,146.0,31.0,1.43421052631579,1.30921052631579,12.1019736842105,4.15131578947368,5.08552631578947,2.86513157894737,53.5789473684211,16.9638157894737,0.414473684210526,0.391447368421053,0.253289473684211,0.355263157894737))
        onTeamCreated(getTeamItem(27,"Sevilla",303,82.0,80.0,82.0,83.0,540.0,28.64,70.0,70.0,80.0,180.0,48.0,1.57755775577558,1.2046204620462,12.8844884488449,4.63696369636964,5.44554455445545,2.8019801980198,54.1749174917492,14.8448844884488,0.570957095709571,0.504950495049505,0.224422442244224,0.270627062706271))
        onTeamCreated(getTeamItem(28,"Valencia",304,78.0,77.0,77.0,78.0,530.0,26.82,40.0,40.0,40.0,248.0,45.0,1.43092105263158,1.24013157894737,11.5197368421053,4.14144736842105,4.82565789473684,2.55263157894737,49.3684210526316,17.0230263157895,0.473684210526316,0.394736842105263,0.282894736842105,0.322368421052632))
        onTeamCreated(getTeamItem(29,"Valladolid",152,75.0,76.0,76.0,74.0,8.9,27.64,40.0,40.0,60.0,43.0,11.0,0.894736842105263,1.38815789473684,10.0592105263158,3.30263157894737,4.53947368421053,2.21710526315789,45.7565789473684,15.9868421052632,0.296052631578947,0.203947368421053,0.375,0.421052631578947))
        onTeamCreated(getTeamItem(30,"Villarreal",304,80.0,83.0,79.0,79.0,370.0,27.09,60.0,50.0,60.0,123.0,36.0,1.4375,1.13157894736842,11.7796052631579,4.33881578947368,4.71381578947368,2.72697368421053,49.8190789473684,14.7927631578947,0.292763157894737,0.430921052631579,0.263157894736842,0.305921052631579))
    }
}