import kotlinx.coroutines.*
import java.io.File
import javax.sound.sampled.AudioSystem
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TotallerTest {
    @Test
    fun 'should be able to add 3 and 4'() {
        val totaller = Totaller()

        assertEquals(3, totaller.add(3))
        assertEquals(7, totaller.add(4))
        assertEquals(7, totaller.total)
    }
}

class AnotherTotallerTest : StringSpec({
    "should be able to add 3 and 4 - and it mustn't go wrong" {
        val totaller = Totaller()

        totaller.add(3) shouldBe 3
        totaller.add(4) shouldBe 7
        totaller.total shouldBe 7
    }

    "should be able to add lots of different numbers" {
        forall(
            row(1, 2, 3),
            row(19, 47, 66),
            row(11, 21, 32)
        ) { x, y, expectedTotal ->
            val totaller =Totaller(x)
            totaller.add(y) shouldBe expectedTotal
        }
    }
})

class Totaller(var total: Int = 0) {
    fun add(num: Int) : Int {
        total += num
        return total
    }
}


suspend fun playBeats(beats: String, file: String) {
    val parts = beats.split("x")
    var count = 0
    for (part in parts) {
        count += part.length + 1
        if (part == "") {
            playSound(file)
        } else {
            delay(100 * (part.length + 1L))
            if (count < beats.length) {
                playSound(file)
            }
        }
    }
}

fun playSound (file: String) {
    val clip = AudioSystem.getClip()
    val audioInputStream = AudioSystem.getAudioInputStream(
        File(
            file
        )
    )
    clip.open(audioInputStream)
    clip.start()
}

suspend fun main() {
    runBlocking {
        launch { playBeats("x-x-x-x-x-x-", "toms.aiff") }
        playBeats("x-----x-----", "crash_cymbal.aiff")
    }
}