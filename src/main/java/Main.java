import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Main
{
    static int[] digestBitLength = {8, 12, 16, 20};
    static int rounds = 50;
    static int lengthsToTest = 4;

    public static void main(String arg[])
    {

        TruncatedSHA1 sha1_1 = new TruncatedSHA1();
        TruncatedSHA1 sha1_2 = new TruncatedSHA1();
        List<Byte> result1, result2;
        int triesToSuccess;

        LinkedList<LinkedList<Integer>> totalRuntimesByLengthCollision = new LinkedList<>();
        LinkedList<LinkedList<Integer>> totalRunTimesByLengthPreImage = new LinkedList<>();
        LinkedList<Integer> averageRuntimesByLengthCollision = new LinkedList<>();
        LinkedList<Integer> averageRunTimesByLengthPreImage = new LinkedList<>();

        for (int length = 0; length < lengthsToTest; length++)
        {
            LinkedList<Integer> currentLengthResultListCollision = new LinkedList<>();
            LinkedList<Integer> currentLengthResultListPreImage = new LinkedList<>();

            for (int round = 0; round < rounds; round++)
            {
                triesToSuccess = 0;
                do {
                    triesToSuccess++;
                    result1 = sha1_1.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);
                    result2 = sha1_2.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);
                } while (!result1.equals(result2));
                currentLengthResultListCollision.add(triesToSuccess);

                result1 = sha1_1.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);

                triesToSuccess = 0;
                do {
                   triesToSuccess++;
                   result2 = sha1_2.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);
                } while(!result1.equals(result2));
                currentLengthResultListPreImage.add(triesToSuccess);
            }
            totalRuntimesByLengthCollision.add(currentLengthResultListCollision);
            totalRunTimesByLengthPreImage.add(currentLengthResultListPreImage);

            long totalTriesCollision = 0;
            long totalTriesPreImage = 0;
            for(int i = 0; i < rounds; i++)
            {
                totalTriesCollision += currentLengthResultListCollision.get(i);
                totalTriesPreImage += currentLengthResultListPreImage.get(i);
            }

            averageRuntimesByLengthCollision.add((int)(totalTriesCollision / rounds));
            averageRunTimesByLengthPreImage.add((int)(totalTriesPreImage / rounds));
        }

        for(int length = 0; length < lengthsToTest; length++)
        {
            System.out.print("Average collision try for key length ");
            System.out.println(digestBitLength[length]);
            System.out.println(averageRuntimesByLengthCollision.get(length));
            System.out.println("Actual collision tries are:");
            List<Integer> LengthResultsCollision = totalRuntimesByLengthCollision.get(length);

            for(Integer result : LengthResultsCollision)
            {
                System.out.println(result);
            }

            System.out.print("Average preimage try for key length ");
            System.out.println(digestBitLength[length]);
            System.out.println(averageRunTimesByLengthPreImage.get(length));
            System.out.println("Actual collision tries are:");
            List<Integer> lengthResultsPreImage = totalRunTimesByLengthPreImage.get(length);

            for(Integer result : lengthResultsPreImage)
            {
                System.out.println(result);
            }
        }
    }

    /**
     * This helper function prints a list of bytes as a single continuous hex string.
     * The output is of the format '0x{...}', with the entire array of bytes as hex following.
     * Basically just used for sanity checks.
     * @param byteList The list of bytes to print as a string.
     */
    public static void printBytesAsHex(List<Byte> byteList)
    {
        StringBuilder sb = new StringBuilder();

        for (Byte b : byteList)
        {
            sb.append(String.format("%02X", b));
        }

        System.out.print("0x");
        System.out.println(sb);
    }
}
