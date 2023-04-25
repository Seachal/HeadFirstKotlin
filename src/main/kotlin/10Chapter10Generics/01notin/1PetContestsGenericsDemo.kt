/* From the "Headfirst Kotlin" book.
 * This code is meant to illustrate the use of generics, and generic types:
 * covariant, contravariant and invariant.
 */

// 宠物
abstract class Pet(var name: String)

// 具体的宠物
class Cat(name: String) : Pet(name)
class Dog(name: String) : Pet(name)
class Fish(name: String) : Pet(name)


// 兽医 ，
class Vet<T: Pet> {  //  T: Pet 就是为了限制，这是宠物医生，不能给人看病
    fun treat(t: T) {
        println("Treat Pet ${t.name}")
    }
}




// 比赛，竞赛
class Contest<T: Pet>(var vet: Vet<in T>) {
//    成绩表
    val scores: MutableMap<T, Int> = mutableMapOf()

    fun addScore(t: T, score: Int = 0) {
        if (score >= 0) scores[t] = score
    }

//   获胜者
    fun getWinners(): MutableSet<T> {
        val winners: MutableSet<T> = mutableSetOf()
        val highScore = scores.values.maxOrNull<Int>() ?: 0

        for((t, score) in scores) {
            if (score == highScore) winners.add(t)
        }

        return winners
    }
}


// （宠物）零售商
interface Retailer<out T> {
    fun sell(): T
}

// 猫零售商
class CatRetailer : Retailer<Cat> {
    override fun sell(): Cat {
        println("Sell Cat")
        return Cat("")
    }
}

// 狗零售商
class DogRetailer : Retailer<Dog> {
    override fun sell(): Dog {
        println("Sell Dog")
        return Dog("")
    }
}

//鱼零售商
class FishRetailer : Retailer<Fish> {
    override fun sell(): Fish {
        println("Sell Fish")
        return Fish("")
    }
}



fun main(args: Array<String>) {
    val catFuzz = Cat("Fuzz Lightyear")
    val catKatsu = Cat("Katsu")
    val fishFinny = Fish("Finny McGraw")

    val catVet = Vet<Cat>()
    val fishVet = Vet<Fish>()
    val petVet = Vet<Pet>()

    catVet.treat(catFuzz)
    petVet.treat(catKatsu)
    petVet.treat(fishFinny)


//   宠物猫比赛
    val catContest = Contest<Cat>(catVet)
    catContest.addScore(catFuzz, 50)
    catContest.addScore(catKatsu, 45)
    val topCat = catContest.getWinners().first()
    println("Cat contest winner is ${topCat.name}")


//    宠物比赛，  更通用
//    Pet<Vet> 代替 Pet<Cat>
    val petContest = Contest<Pet>(petVet)
    petContest.addScore(catFuzz, 50)
    petContest.addScore(fishFinny, 56)
    val topPet = petContest.getWinners().first()
    println("Pet contest winnerr is ${topPet.name}")

    val fishContest = Contest<Fish>(petVet)

    val dogRetailer: Retailer<Dog> = DogRetailer()
    val catRetailer: Retailer<Cat> = CatRetailer()
    val petRetailer: Retailer<Pet> = CatRetailer()
    petRetailer.sell()
}