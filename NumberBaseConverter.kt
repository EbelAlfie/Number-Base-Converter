package converter

import java.math.BigInteger
import java.math.BigDecimal
import java.math.RoundingMode

fun convert(sourceBase: Int, targetBase: Int, number: String) {
    val input = convertToDecimal(number, sourceBase)
    print(input)
    print("Conversion result: ")
    val remainder = convertFromDecimal(input, targetBase)
    if (number.contains('.') && remainder.lastIndex < 4) {
        print(".")
        for (i in 1..4 - remainder.lastIndex) {
            print("0")
        }
    }
    println()
}

fun convertToDecimal (raw: String, base: Int): BigDecimal {
    var power : Int = 0
    var input = raw
    var decimal: BigDecimal = BigDecimal.ZERO
    if (input.contains('.')) {
        input = raw.toString().dropLastWhile{it == '0'}
        for (i in input.lastIndex downTo 0) {
            if (input[i].equals('.'))
            {
                power = -1 * (input.lastIndex - i)
            }
        }
    }
    for (i in input.lastIndex downTo 0) {
        if (input[i].equals('.')) {
            continue ;
        }  
        var num = BigDecimal.ONE.divide(base.toBigDecimal().pow(Math.abs(power)), 10, RoundingMode.CEILING)
        if (input[i].isDigit()) {
            when{
                power >= 0 -> decimal += (input[i].code - 48).toBigDecimal() * base.toBigDecimal().pow(power)
                power < 0 -> decimal += (input[i].code - 48).toBigDecimal() * num
            }
        }else {
            when{
                power >= 0 -> decimal += (input[i].code - 87).toBigDecimal() * base.toBigDecimal().pow(power)
                power < 0 -> decimal += (input[i].code - 87).toBigDecimal() * num
            }
        }  
        power++
        //println(power)
    }   
    return decimal
}

fun convertFromDecimal (input: BigDecimal, base: Int): String {
    //var remainder: MutableList<Char> = mutableListOf()
    var remainder : String = ""
    val num = input.toString().split('.').toTypedArray()
    val checker = input.toString().contains('.')
    
    var fraction: BigDecimal = BigDecimal.ZERO
    var integer = num[0].toBigInteger()
    if (checker == true) {
        fraction = "0.${num[1]}".toBigDecimal()
    }
    while (integer != BigInteger.ZERO){
        if(integer.mod(base.toBigInteger()).toInt() > 9){     
            remainder +=((integer.mod(base.toBigInteger())).mod(BigInteger.TEN).toInt() + 55 + (integer.mod(base.toBigInteger()).toInt() / 10) * 10).toChar()
        }else {
            remainder += integer.mod(base.toBigInteger()).toInt().toString().get(0)
        }
        integer = integer / base.toBigInteger()
    }
    print(remainder.reversed())
    remainder = ""
    if (checker == true) {
        print('.')
        var scales: Int = 0 
        while (fraction != BigDecimal.ZERO && scales < 5) {
            var proc = (fraction * base.toBigDecimal())
            remainder += if (proc.toInt() > 9) (proc.toInt() + 55).toChar() else proc.toInt().toString().get(0)
            fraction = proc - proc.toInt().toBigDecimal()
            scales += 1
        }
        print(remainder)
    }
    return remainder
}

fun main() { 
    do {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val bases = readln()
        when (bases) {
            "/exit" -> return
            else -> {
                do {
                    val (sourceBase, targetBase) = bases.split(" ")
                    println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
                    val number = readln()
                    when(number) {
                        "/back" -> break
                        else -> convert(sourceBase.toInt(), targetBase.toInt(), number)
                    }
                } while(!number.equals("/back")) 
            }
        }
        println()
    } while(!bases.equals("/exit"))
}