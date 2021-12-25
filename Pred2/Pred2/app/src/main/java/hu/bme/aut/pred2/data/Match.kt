package hu.bme.aut.pred2.data

import java.util.*

data class Match (
    var date: Date,
    var homeTeam: Team,
    var awayTeam: Team,
    var bethomeodds: Float,
    var betdrawodds: Float,
    var betawayodds: Float
    )
