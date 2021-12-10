package hu.bme.aut.pred2

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.databinding.FragmentPictureDialogBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PictureDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentPictureDialogBinding
    private var team: Team? = null
    private var number: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        number = arguments?.getInt(ITEMS) ?: throw IllegalStateException("No args provided")
    }
    companion object {

        private const val ITEMS = "items"

        fun newInstance(
            number: Int
        ): PictureDialogFragment = PictureDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(ITEMS, number)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPictureDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //reference layout elements by name freely
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        when (number) {
            0 ->binding.prematchcorrIV.setImageResource(R.drawable.alavesprematchcorr)
            1 ->binding.prematchcorrIV.setImageResource(R.drawable.almeriaprematchcorr)
            2 ->binding.prematchcorrIV.setImageResource(R.drawable.ath_bilbaoprematchcorr)
            3 ->binding.prematchcorrIV.setImageResource(R.drawable.atl_madridprematchcorr)
            4 ->binding.prematchcorrIV.setImageResource(R.drawable.barcelonaprematchcorr)
            5 ->binding.prematchcorrIV.setImageResource(R.drawable.betisprematchcorr)
            6 ->binding.prematchcorrIV.setImageResource(R.drawable.cadiz_cfprematchcorr)
            7 ->binding.prematchcorrIV.setImageResource(R.drawable.celta_vigoprematchcorr)
            8 ->binding.prematchcorrIV.setImageResource(R.drawable.cordobaprematchcorr)
            9 ->binding.prematchcorrIV.setImageResource(R.drawable.dep_la_corunaprematchcorr)
            10 ->binding.prematchcorrIV.setImageResource(R.drawable.eibarprematchcorr)
            11 ->binding.prematchcorrIV.setImageResource(R.drawable.elcheprematchcorr)
            12 ->binding.prematchcorrIV.setImageResource(R.drawable.espanyolprematchcorr)
            13 ->binding.prematchcorrIV.setImageResource(R.drawable.getafeprematchcorr)
            14 ->binding.prematchcorrIV.setImageResource(R.drawable.gijonprematchcorr)
            15 ->binding.prematchcorrIV.setImageResource(R.drawable.gironaprematchcorr)
            16 ->binding.prematchcorrIV.setImageResource(R.drawable.granada_cfprematchcorr)
            17 ->binding.prematchcorrIV.setImageResource(R.drawable.huescaprematchcorr)
            18 ->binding.prematchcorrIV.setImageResource(R.drawable.las_palmasprematchcorr)
            19 ->binding.prematchcorrIV.setImageResource(R.drawable.leganesprematchcorr)
            20 ->binding.prematchcorrIV.setImageResource(R.drawable.levanteprematchcorr)
            21 ->binding.prematchcorrIV.setImageResource(R.drawable.malagaprematchcorr)
            22 ->binding.prematchcorrIV.setImageResource(R.drawable.mallorcaprematchcorr)
            23 ->binding.prematchcorrIV.setImageResource(R.drawable.osasunaprematchcorr)
            24 ->binding.prematchcorrIV.setImageResource(R.drawable.rayo_vallecanoprematchcorr)
            25 ->binding.prematchcorrIV.setImageResource(R.drawable.real_madridprematchcorr)
            26 ->binding.prematchcorrIV.setImageResource(R.drawable.real_sociedadprematchcorr)
            27 ->binding.prematchcorrIV.setImageResource(R.drawable.sevillaprematchcorr)
            28 ->binding.prematchcorrIV.setImageResource(R.drawable.valenciaprematchcorr)
            29 ->binding.prematchcorrIV.setImageResource(R.drawable.valladolidprematchcorr)
            30 ->binding.prematchcorrIV.setImageResource(R.drawable.villarrealprematchcorr)
            100 ->binding.prematchcorrIV.setImageResource(R.drawable.alavesaftmatchcorr)
            101 ->binding.prematchcorrIV.setImageResource(R.drawable.almeriaaftmatchcorr);
            102 ->binding.prematchcorrIV.setImageResource(R.drawable.ath_bilbaoaftmatchcorr)
            103 ->binding.prematchcorrIV.setImageResource(R.drawable.atl_madridaftmatchcorr)
            104 ->binding.prematchcorrIV.setImageResource(R.drawable.barcelonaaftmatchcorr)
            105 ->binding.prematchcorrIV.setImageResource(R.drawable.betisaftmatchcorr)
            106 ->binding.prematchcorrIV.setImageResource(R.drawable.cadiz_cfaftmatchcorr)
            107 ->binding.prematchcorrIV.setImageResource(R.drawable.celta_vigoaftmatchcorr)
            108 ->binding.prematchcorrIV.setImageResource(R.drawable.cordobaaftmatchcorr)
            109 ->binding.prematchcorrIV.setImageResource(R.drawable.dep_la_corunaaftmatchcorr)
            110 ->binding.prematchcorrIV.setImageResource(R.drawable.eibaraftmatchcorr)
            111 ->binding.prematchcorrIV.setImageResource(R.drawable.elcheaftmatchcorr)
            112 ->binding.prematchcorrIV.setImageResource(R.drawable.espanyolaftmatchcorr)
            113 ->binding.prematchcorrIV.setImageResource(R.drawable.getafeaftmatchcorr)
            114 ->binding.prematchcorrIV.setImageResource(R.drawable.gijonaftmatchcorr)
            115 ->binding.prematchcorrIV.setImageResource(R.drawable.gironaaftmatchcorr)
            116 ->binding.prematchcorrIV.setImageResource(R.drawable.granada_cfaftmatchcorr)
            117 ->binding.prematchcorrIV.setImageResource(R.drawable.huescaaftmatchcorr)
            118 ->binding.prematchcorrIV.setImageResource(R.drawable.las_palmasaftmatchcorr)
            119 ->binding.prematchcorrIV.setImageResource(R.drawable.leganesaftmatchcorr)
            120 ->binding.prematchcorrIV.setImageResource(R.drawable.levanteaftmatchcorr)
            121 ->binding.prematchcorrIV.setImageResource(R.drawable.malagaaftmatchcorr)
            122 ->binding.prematchcorrIV.setImageResource(R.drawable.mallorcaaftmatchcorr)
            123 ->binding.prematchcorrIV.setImageResource(R.drawable.osasunaaftmatchcorr)
            124 ->binding.prematchcorrIV.setImageResource(R.drawable.rayo_vallecanoaftmatchcorr)
            125 ->binding.prematchcorrIV.setImageResource(R.drawable.real_madridaftmatchcorr)
            126 ->binding.prematchcorrIV.setImageResource(R.drawable.real_sociedadaftmatchcorr)
            127 ->binding.prematchcorrIV.setImageResource(R.drawable.sevillaaftmatchcorr)
            128 ->binding.prematchcorrIV.setImageResource(R.drawable.valenciaaftmatchcorr)
            129 ->binding.prematchcorrIV.setImageResource(R.drawable.valladolidaftmatchcorr)
            130 ->binding.prematchcorrIV.setImageResource(R.drawable.villarrealaftmatchcorr)
            200 ->binding.prematchcorrIV.setImageResource(R.drawable.alavesstat)
            201 ->binding.prematchcorrIV.setImageResource(R.drawable.almeriastat)
            202 ->binding.prematchcorrIV.setImageResource(R.drawable.ath_bilbaostat)
            203 ->binding.prematchcorrIV.setImageResource(R.drawable.atl_madridstat)
            204 ->binding.prematchcorrIV.setImageResource(R.drawable.barcelonastat)
            205 ->binding.prematchcorrIV.setImageResource(R.drawable.betisstat)
            206 ->binding.prematchcorrIV.setImageResource(R.drawable.cadiz_cfstat)
            207 ->binding.prematchcorrIV.setImageResource(R.drawable.celta_vigostat)
            208 ->binding.prematchcorrIV.setImageResource(R.drawable.cordobastat)
            209 ->binding.prematchcorrIV.setImageResource(R.drawable.dep_la_corunastat)
            210 ->binding.prematchcorrIV.setImageResource(R.drawable.eibarstat)
            211 ->binding.prematchcorrIV.setImageResource(R.drawable.elchestat)
            212 ->binding.prematchcorrIV.setImageResource(R.drawable.espanyolstat)
            213 ->binding.prematchcorrIV.setImageResource(R.drawable.getafestat)
            214 ->binding.prematchcorrIV.setImageResource(R.drawable.gijonstat)
            215 ->binding.prematchcorrIV.setImageResource(R.drawable.gironastat)
            216 ->binding.prematchcorrIV.setImageResource(R.drawable.granada_cfstat)
            217 ->binding.prematchcorrIV.setImageResource(R.drawable.huescastat)
            218 ->binding.prematchcorrIV.setImageResource(R.drawable.las_palmasstat)
            219 ->binding.prematchcorrIV.setImageResource(R.drawable.leganesstat)
            220 ->binding.prematchcorrIV.setImageResource(R.drawable.levantestat)
            221 ->binding.prematchcorrIV.setImageResource(R.drawable.malagastat)
            222 ->binding.prematchcorrIV.setImageResource(R.drawable.mallorcastat)
            223 ->binding.prematchcorrIV.setImageResource(R.drawable.osasunastat)
            224 ->binding.prematchcorrIV.setImageResource(R.drawable.rayo_vallecanostat)
            225 ->binding.prematchcorrIV.setImageResource(R.drawable.real_madridstat)
            226 ->binding.prematchcorrIV.setImageResource(R.drawable.real_sociedadstat)
            227 ->binding.prematchcorrIV.setImageResource(R.drawable.sevillastat)
            228 ->binding.prematchcorrIV.setImageResource(R.drawable.valenciastat)
            229 ->binding.prematchcorrIV.setImageResource(R.drawable.valladolidstat)
            230 ->binding.prematchcorrIV.setImageResource(R.drawable.villarrealstat)
            else -> binding.prematchcorrIV.setImageResource(R.drawable.barcelonastat)
        }
    }

}
