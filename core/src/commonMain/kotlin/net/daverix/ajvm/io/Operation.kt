package net.daverix.ajvm.io

interface OffsetOperation {
    val byteCodeIndex: Int
    val offset: Int
}

sealed class Operation {
    object Return : Operation()
    object IReturn : Operation()
    object FReturn : Operation()
    object LReturn : Operation()
    object DReturn : Operation()
    object AReturn : Operation()
    object NOP : Operation()
    object AConstNull : Operation()
    data class IConst(val value: Int) : Operation()
    data class LConst(val value: Long) : Operation()
    data class FConst(val value: Float) : Operation()
    data class DConst(val value: Double) : Operation()
    data class Ldc(val index: Int) : Operation()
    data class Ldc2(val index: Int) : Operation()
    data class ILoad(val index: Int) : Operation()
    data class LLoad(val index: Int) : Operation()
    data class FLoad(val index: Int) : Operation()
    data class DLoad(val index: Int) : Operation()
    data class ALoad(val index: Int) : Operation()
    object IALoad : Operation()
    object LALoad : Operation()
    object FALoad : Operation()
    object DALoad : Operation()
    object AALoad : Operation()
    object BALoad : Operation()
    object CALoad : Operation()
    object SALoad : Operation()

    data class IStore(val index: Int) : Operation()
    data class LStore(val index: Int) : Operation()
    data class FStore(val index: Int) : Operation()
    data class DStore(val index: Int) : Operation()
    data class AStore(val index: Int) : Operation()

    object IAStore : Operation()
    object LAStore : Operation()
    object FAStore : Operation()
    object DAStore : Operation()
    object AAStore : Operation()
    object BAStore : Operation()
    object CAStore : Operation()
    object SAStore : Operation()

    object Pop : Operation()
    object Pop2 : Operation()
    object Dup : Operation()
    object DupX1 : Operation()
    object DupX2 : Operation()
    object Dup2 : Operation()
    object Dup2X1 : Operation()
    object Dup2X2 : Operation()
    object Swap : Operation()

    object IAdd : Operation()
    object LAdd : Operation()
    object FAdd : Operation()
    object DAdd : Operation()

    object ISub : Operation()
    object LSub : Operation()
    object FSub : Operation()
    object DSub : Operation()

    object IMul : Operation()
    object LMul : Operation()
    object FMul : Operation()
    object DMul : Operation()

    object IDiv : Operation()
    object LDiv : Operation()
    object FDiv : Operation()
    object DDiv : Operation()

    object IRem : Operation()
    object LRem : Operation()
    object FRem : Operation()
    object DRem : Operation()

    object INeg : Operation()
    object LNeg : Operation()
    object FNeg : Operation()
    object DNeg : Operation()

    object IShl : Operation()
    object IShr : Operation()
    object LShl : Operation()
    object LShr : Operation()
    object IUshr : Operation()
    object LUshr : Operation()
    object IAnd : Operation()
    object LAnd : Operation()
    object IOr : Operation()
    object LOr : Operation()
    object IXor : Operation()
    object LXor : Operation()

    data class IInc(val index: Int, val const: Int) : Operation()
    object IntToLong : Operation()
    object IntToFloat : Operation()
    object IntToDouble : Operation()
    object IntToByte : Operation()
    object IntToChar : Operation()
    object IntToShort : Operation()
    object LongToInt : Operation()
    object LongToFloat : Operation()
    object LongToDouble : Operation()
    object FloatToInt : Operation()
    object FloatToLong : Operation()
    object FloatToDouble : Operation()
    object DoubleToInt : Operation()
    object DoubleToLong : Operation()
    object DoubleToFloat : Operation()

    object LCmp : Operation()
    object FCmpL : Operation()
    object FCmpG : Operation()
    object DCmpL : Operation()
    object DCmpG : Operation()

    data class IIfEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IIfNotEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IIfLessThan(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IIfGreaterThan(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IIfLessThanOrEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IIfGreaterThanOrEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareIntEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareIntNotEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareIntLessThan(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareIntGreaterThanOrEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareIntLessThanOrEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareReferenceEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfCompareReferenceNotEqual(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class Goto(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class JumpToSubroutine(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class Ret(val index: Int) : Operation()

    data class TableSwitch(
            val byteCodeIndex: Int,
            val defaultValue: Int,
            val low: Int,
            val high: Int,
            val table: List<Int>
    ) : Operation()

    data class LookupSwitch(
            val byteCodeIndex: Int,
            val defaultValue: Int,
            val pairs: Map<Int, Int>
    ) : Operation()

    data class GetStatic(val field: Int) : Operation()
    data class PutStatic(val field: Int) : Operation()
    data class GetField(val field: Int) : Operation()
    data class PutField(val field: Int) : Operation()
    data class InvokeVirtual(val methodReferenceIndex: Int) : Operation()
    data class InvokeSpecial(val methodReferenceIndex: Int) : Operation()
    data class InvokeStatic(val methodReferenceIndex: Int) : Operation()
    data class InvokeInterface(val methodReferenceIndex: Int) : Operation()
    data class InvokeDynamic(val methodReferenceIndex: Int) : Operation()
    data class New(val classReferenceIndex: Int) : Operation()
    data class NewArray(val type: Int) : Operation()
    data class ANewArray(val classReferenceIndex: Int) : Operation()
    object ArrayLength : Operation()
    object AThrow : Operation()

    data class CheckCast(val classReferenceIndex: Int) : Operation()
    data class InstanceOf(val classReferenceIndex: Int) : Operation()
    object MonitorEnter : Operation()
    object MonitorExit : Operation()

    data class MultiANewArray(val classIndex: Int, val dimensions: Int) : Operation()
    data class IfNull(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation

    data class IfNonNull(
            override val byteCodeIndex: Int,
            override val offset: Int
    ) : Operation(), OffsetOperation
}
