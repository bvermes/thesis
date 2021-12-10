package hu.bme.aut.pred2.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.KProperty


//Egy csapatot reprezentáló osztály
@Entity(tableName = "team")
data class Team(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "field1") var field1: Int,
    @ColumnInfo(name = "teamname") var teamname: String,
    @ColumnInfo(name = "matches_played") var matches_played: Int,
    @ColumnInfo(name = "overall") var overall: Int,
    @ColumnInfo(name = "AttackingRating") var attackingRating: Float,
    @ColumnInfo(name = "MidfieldRating") var midfieldRating: Float,
    @ColumnInfo(name = "DefenceRating") var defenceRating: Float,
    @ColumnInfo(name = "ClubWorth") var clubWorth: Float,
    @ColumnInfo(name = "XIAverageAge") var xIAverageAge: Float,
    @ColumnInfo(name = "DefenceWidth") var defenceWidth: Float,
    @ColumnInfo(name = "DefenceDepth") var defenceDepth: Float,
    @ColumnInfo(name = "OffenceWidth") var offenceWidth: Float,
    @ColumnInfo(name = "Likes") var likes: Float,
    @ColumnInfo(name = "Dislikes") var dislikes: Float,
    @ColumnInfo(name = "avgoals") var avgoals: Float,
    @ColumnInfo(name = "avconceded") var avconceded: Float,
    @ColumnInfo(name = "avgoalattempts") var avgoalattempts: Float,
    @ColumnInfo(name = "avshotsongoal") var avshotsongoal: Float,
    @ColumnInfo(name = "avshotsoffgoal") var avshotsoffgoal: Float,
    @ColumnInfo(name = "avblockedshots") var avblockedshots: Float,
    @ColumnInfo(name = "avpossession") var avpossession: Float,
    @ColumnInfo(name = "avfreekicks") var avfreekicks: Float,
    @ColumnInfo(name = "avGoalDiff") var avGoalDiff: Float,
    @ColumnInfo(name = "avwins") var avwins: Float,
    @ColumnInfo(name = "avdraws") var avdraws: Float,
    @ColumnInfo(name = "avloses") var avloses: Float,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(field1)
        parcel.writeString(teamname)
        parcel.writeInt(matches_played)
        parcel.writeInt(overall)
        parcel.writeFloat(attackingRating)
        parcel.writeFloat(midfieldRating)
        parcel.writeFloat(defenceRating)
        parcel.writeFloat(clubWorth)
        parcel.writeFloat(xIAverageAge)
        parcel.writeFloat(defenceWidth)
        parcel.writeFloat(defenceDepth)
        parcel.writeFloat(offenceWidth)
        parcel.writeFloat(likes)
        parcel.writeFloat(dislikes)
        parcel.writeFloat(avgoals)
        parcel.writeFloat(avconceded)
        parcel.writeFloat(avgoalattempts)
        parcel.writeFloat(avshotsongoal)
        parcel.writeFloat(avshotsoffgoal)
        parcel.writeFloat(avblockedshots)
        parcel.writeFloat(avpossession)
        parcel.writeFloat(avfreekicks)
        parcel.writeFloat(avGoalDiff)
        parcel.writeFloat(avwins)
        parcel.writeFloat(avdraws)
        parcel.writeFloat(avloses)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Team> {
        override fun createFromParcel(parcel: Parcel): Team {
            return Team(parcel)
        }

        override fun newArray(size: Int): Array<Team?> {
            return arrayOfNulls(size)
        }
    }
}