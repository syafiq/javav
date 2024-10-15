    private static long copyLastLiteral(Object outputBase, Object literalsBase, long literalsLimit, long output, long literalsInput)
    {
        long lastLiteralsSize = literalsLimit - literalsInput;
        UNSAFE.copyMemory(literalsBase, literalsInput, outputBase, output, lastLiteralsSize);
        output += lastLiteralsSize;
        return output;
    }