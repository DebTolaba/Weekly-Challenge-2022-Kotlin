package com.mouredev.weeklychallenge2022

/*
 * Reto #15
 * ¿CUÁNTOS DÍAS?
 * Fecha publicación enunciado: 11/04/22
 * Fecha publicación resolución: 18/04/22
 * Dificultad: DIFÍCIL
 *
 * Enunciado: Crea una función que calcule y retorne cuántos días hay entre dos cadenas de texto que representen fechas.
 * - Una cadena de texto que representa una fecha tiene el formato "dd/MM/yyyy".
 * - La función recibirá dos String y retornará un Int.
 * - La diferencia en días será absoluta (no importa el orden de las fechas).
 * - Si una de las dos cadenas de texto no representa una fecha correcta se lanzará una excepción.
 *
 * Información adicional:
 * - Usa el canal de nuestro discord (https://mouredev.com/discord) "🔁reto-semanal" para preguntas, dudas o prestar ayuda a la comunidad.
 * - Puedes hacer un Fork del repo y una Pull Request al repo original para que veamos tu solución aportada.
 * - Revisaré el ejercicio en directo desde Twitch el lunes siguiente al de su publicación.
 * - Subiré una posible solución al ejercicio el lunes siguiente al de su publicación.
 *
 */

fun main(){
    calculateDays("15/12/2021", "27/01/2022") // 43 dias
    calculateDays("07/07/2022", "29/07/2022") // 22 dias
    calculateDays("17/01/2020","17/01/2020") // 0 dias
    calculateDays("01/07/2022","01/12/2022") // 153 dias
    calculateDays("03/01/2020","17/08/2024") // 1688 dias
    calculateDays("03/01/2020","") // fecha incorrecta
    calculateDays("2022/01/01","17/08/2024") // fecha incorrecta
    calculateDays("  /  /   ","17/08/2024") // fecha incorrecta
    calculateDays("03/01/2020","dd/Ñm/202L") // fecha incorrecta
}

fun calculateDays(date1: String, date2: String){
    val time = Time()
    if(time.isDate(date1) and time.isDate(date2)){
        val days = time.getDays(time.getDate(date1), time.getDate(date2))
        println("Cantidad de dias: $days") }

    else println("Error: fecha incorrecta")
}

data class Date(var day: Int, var month: Int, var year: Int)

class Time{
    private val daysOfMonth: MutableMap<Int, Int> = mutableMapOf(
        1 to 31, 2 to 28, 3 to 31, 4 to 30, 5 to 31, 6 to 30,
        7 to 31, 8 to 31, 9 to 30, 10 to 31, 11 to 30, 12 to 31)
    private var daysYear = 365

    fun isDate(sdate: String): Boolean{
        val format = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        if(format.matches(sdate)) {
            val list = sdate.split("/").map { it.toInt() }
            val date = Date(list[0], list[1], list[2])

            if (date.month in 1..12) {
                var lim = daysOfMonth[date.month]
                if((date.month == 2) and (isBisiesto(date.year))) { lim = 29 }
                if((date.day in 1..lim!!)) return true
            }
        }
        return false
    }

    fun getDate(date: String): Date{
        val list = date.split("/").map { it.toInt() }
        return Date(list[0], list[1], list[2])
    }

    fun getDays(date1: Date, date2: Date): Int{
        val (dateStart, dateEnd) = orderDates(date1,date2)

        var days = diffYears(dateStart.year , (dateEnd.year - dateStart.year))
        days += restOfYear(dateStart.month, dateStart.year) - dateStart.day
        days -= restOfYear(dateEnd.month, dateEnd.year) - dateEnd.day
        return days
    }

    private fun diffYears(yearStart: Int, diff: Int): Int{
        var days = 0
        for(i in 1..diff){
            days += if(isBisiesto(yearStart + i)) 366 else 365
        }
        return days
    }

    private fun restOfYear(monthStart: Int, year:Int): Int{
        var days = 0

        if(isBisiesto(year)){  daysYear = 366; daysOfMonth[2] = 29 }
        else{ daysYear = 365; daysOfMonth[2] = 28 }

        if(monthStart == 1) return daysYear
        else{
            for(month in monthStart..12){
                days += daysOfMonth[month]!!
            }
        }

        return days
    }

    private fun orderDates(date1: Date, date2: Date): Pair<Date, Date>{
        var order = Pair(date1,date2)
        if(date1.year != date2.year){
            if(date1.year > date2.year) order = Pair(date2, date1) }

        else if(date1.month != date2.month){
            if(date1.month > date2.month) order = Pair(date2, date1) }

        else if(date1.day > date2.day) order = Pair(date2, date1)

        return order
    }

    private fun isBisiesto(year: Int): Boolean {
        return ((year % 4 == 0) and ((year % 100 != 0) or (year % 400 == 0)))
    }
}