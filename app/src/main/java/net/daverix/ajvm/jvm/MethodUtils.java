package net.daverix.ajvm.jvm;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodUtils {
    private static final Pattern METHOD_PARAMETER_COUNT_PATTERN = Pattern.compile("\\((B|C|D|F|I|J|L.+;|S|Z|\\[.+)*\\)B|C|D|F|I|J|L.+;|S|Z|\\[.+");

    public static int getArgumentCount(String descriptor) {
        if (descriptor.startsWith("()"))
            return 0;

        Matcher matcher = METHOD_PARAMETER_COUNT_PATTERN.matcher(descriptor);
        return matcher.groupCount();
    }
}
