package hu.bme.aut.pred2.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.pred2.R
import hu.bme.aut.pred2.TeamActivity
import hu.bme.aut.pred2.TeamDetailsActivity
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.databinding.TeamListBinding

class TeamAdapter(private val listener: TeamClickListener, val context: Context) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private val items = mutableListOf<Team>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TeamViewHolder(
        TeamListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = items[position]
        when (team.field1) {
            0 ->  holder.binding.ivIcon.setImageResource(R.drawable.alaves)
            1 -> holder.binding.ivIcon.setImageResource(R.drawable.almeria)
            2 ->  holder.binding.ivIcon.setImageResource(R.drawable.ath_bilbao)
            3 -> holder.binding.ivIcon.setImageResource(R.drawable.atl_madrid)
            4 ->  holder.binding.ivIcon.setImageResource(R.drawable.barcelona)
            5 -> holder.binding.ivIcon.setImageResource(R.drawable.betis)
            6 ->  holder.binding.ivIcon.setImageResource(R.drawable.cadiz)
            7 -> holder.binding.ivIcon.setImageResource(R.drawable.celta_vigo)
            8 ->  holder.binding.ivIcon.setImageResource(R.drawable.cordoba)
            9 -> holder.binding.ivIcon.setImageResource(R.drawable.deportivo)
            10 ->  holder.binding.ivIcon.setImageResource(R.drawable.eibar)
            11 -> holder.binding.ivIcon.setImageResource(R.drawable.elche)
            12 ->  holder.binding.ivIcon.setImageResource(R.drawable.espanyol)
            13 -> holder.binding.ivIcon.setImageResource(R.drawable.getafe)
            14 ->  holder.binding.ivIcon.setImageResource(R.drawable.gijon)
            15 -> holder.binding.ivIcon.setImageResource(R.drawable.girona)
            16 ->  holder.binding.ivIcon.setImageResource(R.drawable.granada)
            17 -> holder.binding.ivIcon.setImageResource(R.drawable.huesca)
            18 ->  holder.binding.ivIcon.setImageResource(R.drawable.las_palmas)
            19 -> holder.binding.ivIcon.setImageResource(R.drawable.leganes)
            20 ->  holder.binding.ivIcon.setImageResource(R.drawable.levante)
            21 -> holder.binding.ivIcon.setImageResource(R.drawable.malaga)
            22 ->  holder.binding.ivIcon.setImageResource(R.drawable.mallorca)
            23 -> holder.binding.ivIcon.setImageResource(R.drawable.osasuna)
            24 ->  holder.binding.ivIcon.setImageResource(R.drawable.rayo_vallecano)
            25 -> holder.binding.ivIcon.setImageResource(R.drawable.real_madrid)
            26 ->  holder.binding.ivIcon.setImageResource(R.drawable.real_sociedad)
            27 -> holder.binding.ivIcon.setImageResource(R.drawable.sevilla)
            28 ->  holder.binding.ivIcon.setImageResource(R.drawable.valencia)
            29 -> holder.binding.ivIcon.setImageResource(R.drawable.valladolid)
            30 -> holder.binding.ivIcon.setImageResource(R.drawable.villarreal)
            else -> holder.binding.ivIcon.setImageResource(R.drawable.barcelona)
        }
        holder.binding.tvName.text = team.teamname
        holder.binding.tvOverall.text = team.overall.toString()
        //https://stackoverflow.com/questions/8090459/android-dynamically-change-textview-background-color/8090505
        if(team.overall > 80){
            holder.binding.tvOverall.setBackgroundColor(Color.parseColor("#11CD19"))
        }else if (team.overall <= 80 && team.overall > 74){
            holder.binding.tvOverall.setBackgroundColor(Color.parseColor("#5BE361"))
        }else {
            holder.binding.tvOverall.setBackgroundColor(Color.parseColor("#FDC622"))
        }
        holder.itemView.setOnClickListener({
            //holder.binding.tvName.text = "Szia"
            val intent = Intent(context, TeamDetailsActivity::class.java)
            intent.putExtra("team", team)
            context.startActivity(intent)
        })
    }


    @DrawableRes()
    private fun getImageResource(id: Int): Int {
        return when (id) {
            else ->0
        }
    }

    fun addItem(item: Team) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(teamItems: List<Team>) {
        items.clear()
        items.addAll(teamItems)
        notifyDataSetChanged()
    }

    //fun deletebyname(name: String) {
    //    items.clear()
    //    items.delete(name)
    //    notifyDataSetChanged()
    //}

    override fun getItemCount(): Int = items.size

    interface TeamClickListener {
        fun onItemChanged(item: Team)
    }

    inner class TeamViewHolder(val binding: TeamListBinding) : RecyclerView.ViewHolder(binding.root)
}