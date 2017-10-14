package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;
import java.util.Locale;

public class TableSwitchOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        reader.skip((indexOfBytecode + 1) % 4);
        int defaultValue = reader.readInt();
        int low = reader.readInt();
        int high = reader.readInt();
        if(low > high) {
            throw new IllegalStateException(String.format(Locale.ENGLISH,
                    "low is higher than high: %d > %d", low, high));
        }

        int offsetWidth = high - low + 1;
        int[] table = new int[offsetWidth];
        for (int i = 0; i < offsetWidth; i++) {
            table[i] = reader.readInt();
        }
        int tableIndex = (int) currentFrame.pop();
        int targetAddress;
        if ((tableIndex < low) || (tableIndex > high)) {
            // TODO: why would Math.abs be needed to turn for example -120 into 120 here?
            targetAddress = indexOfBytecode + Math.abs(defaultValue);
        } else {
            targetAddress = indexOfBytecode + table[tableIndex - low];
        }
        reader.jumpTo(targetAddress);
    }
}
