import software.amazon.ion.system.IonBinaryWriterBuilder
import software.amazon.ion.system.IonSystemBuilder
import software.amazon.ion.system.IonTextWriterBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream

private val ion = IonSystemBuilder.standard().build()
private val binBuilder = IonBinaryWriterBuilder.standard()

private fun dump(func: (OutputStream) -> Unit): ByteArray = ByteArrayOutputStream().use {
    func(it)
    it.toByteArray()
}

/**
 * Very simple tool to do some size comparisons for JSON and Ion text/binary.
 */
fun main(args: Array<String>) {
    if (args.size != 2) {
        throw IllegalArgumentException("Usage: IonSpaceCompare FILE SST-NAME")
    }

    // TODO use the IonReader API versus IonValue to support larger files better.

    val raw = File(args[0]).readBytes()
    val dg = ion.loader.load(raw)

    val txtJson = dump {
        IonTextWriterBuilder.json().build(it).use { dg.writeTo(it) }
    }
    val txtIon = dump {
        IonTextWriterBuilder.standard().build(it).use { dg.writeTo(it) }
    }
    val bin = dump {
        binBuilder.build(it).use { dg.writeTo(it) }
    }

    // load the binary so we can grab the local symbol table
    val biv = ion.loader.load(bin)
    val lst = biv[0].symbolTable
    val sst = ion.newSharedSymbolTable(args[1], 1, lst.iterateDeclaredSymbolNames())

    val binSST = dump {
        binBuilder.withImports(sst).build(it).use { dg.writeTo(it) }
    }

    fun size(target: ByteArray) = String.format("%5d", target.size)
    fun ratio(target: ByteArray) = String.format("%.2f", (1.0 - (target.size.toDouble() / txtJson.size)) * 100)

    println("JSON Size:           ${size(txtJson)}")
    println("Ion Text Size:       ${size(txtIon)}, ${ratio(txtIon)}% smaller")
    println("Ion Binary LST Size: ${size(bin)}, ${ratio(bin)}% smaller")
    println("Ion Binary SST Size: ${size(binSST)}, ${ratio(binSST)}% smaller")

}