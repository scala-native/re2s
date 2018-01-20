package re2s

import java.util.HashMap

class CharGroup(val sign: Int, val cls: Array[Int])

object CharGroup {
  private val code1  = Array(0x30, 0x39)                                     // \d
  private val code2  = Array(0x9, 0xa, 0xc, 0xd, 0x20, 0x20)                 // \s
  private val code3  = Array(0x30, 0x39, 0x41, 0x5a, 0x5f, 0x5f, 0x61, 0x7a) // \w
  private val code4  = Array(0x30, 0x39, 0x41, 0x5a, 0x61, 0x7a)             // [:alnum:]
  private val code5  = Array(0x41, 0x5a, 0x61, 0x7a)                         // [:alpha:]
  private val code6  = Array(0x0, 0x7f)                                      // [:ascii:]
  private val code7  = Array(0x9, 0x9, 0x20, 0x20)                           // [:blank:]
  private val code8  = Array(0x0, 0x1f, 0x7f, 0x7f)                          // [:cntrl:]
  private val code9  = Array(0x30, 0x39)                                     // [:digit:]
  private val code10 = Array(0x21, 0x7e)                                     // [:graph:]
  private val code11 = Array(0x61, 0x7a)                                     // [:lower:]
  private val code12 = Array(0x20, 0x7e)                                     // [:print:]
  private val code13 = Array(0x21, 0x2f, 0x3a, 0x40, 0x5b, 0x60, 0x7b, 0x7e) // [:punct:]
  private val code14 = Array(0x9, 0xd, 0x20, 0x20)                           // [:space:]
  private val code15 = Array(0x41, 0x5a)                                     // [:upper:]
  private val code16 = Array(0x30, 0x39, 0x41, 0x5a, 0x5f, 0x5f, 0x61, 0x7a) // [:word:]
  private val code17 = Array(0x30, 0x39, 0x41, 0x46, 0x61, 0x66)             // [:xdigit:]

  val PERL_GROUPS = Map(
    "\\d" -> new CharGroup(+1, code1),
    "\\D" -> new CharGroup(-1, code1),
    "\\s" -> new CharGroup(+1, code2),
    "\\S" -> new CharGroup(-1, code2),
    "\\w" -> new CharGroup(+1, code3),
    "\\W" -> new CharGroup(-1, code3)
  )

  val POSIX_GROUPS = Map(
    "[:alnum:]"   -> new CharGroup(+1, code4),
    "[:^alnum:]"  -> new CharGroup(-1, code4),
    "[:alpha:]"   -> new CharGroup(+1, code5),
    "[:^alpha:]"  -> new CharGroup(-1, code5),
    "[:ascii:]"   -> new CharGroup(+1, code6),
    "[:^ascii:]"  -> new CharGroup(-1, code6),
    "[:blank:]"   -> new CharGroup(+1, code7),
    "[:^blank:]"  -> new CharGroup(-1, code7),
    "[:cntrl:]"   -> new CharGroup(+1, code8),
    "[:^cntrl:]"  -> new CharGroup(-1, code8),
    "[:digit:]"   -> new CharGroup(+1, code9),
    "[:^digit:]"  -> new CharGroup(-1, code9),
    "[:graph:]"   -> new CharGroup(+1, code10),
    "[:^graph:]"  -> new CharGroup(-1, code10),
    "[:lower:]"   -> new CharGroup(+1, code11),
    "[:^lower:]"  -> new CharGroup(-1, code11),
    "[:print:]"   -> new CharGroup(+1, code12),
    "[:^print:]"  -> new CharGroup(-1, code12),
    "[:punct:]"   -> new CharGroup(+1, code13),
    "[:^punct:]"  -> new CharGroup(-1, code13),
    "[:space:]"   -> new CharGroup(+1, code14),
    "[:^space:]"  -> new CharGroup(-1, code14),
    "[:upper:]"   -> new CharGroup(+1, code15),
    "[:^upper:]"  -> new CharGroup(-1, code15),
    "[:word:]"    -> new CharGroup(+1, code16),
    "[:^word:]"   -> new CharGroup(-1, code16),
    "[:xdigit:]"  -> new CharGroup(+1, code17),
    "[:^xdigit:]" -> new CharGroup(-1, code17)
  )
}
