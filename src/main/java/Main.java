import java.util.*;

public class Main
{
    static int[] digestBitLength = {8, 12, 16, 20};
    static int rounds = 50;
    static int lengthsToTest = 4;
    static String preImage = "Matthew10:26,12:36";

    public static void main(String arg[])
    {

        TruncatedSHA1 sha1_1 = new TruncatedSHA1();
        List<Byte> result1, preImageDigest;
        int triesToSuccess;
        Set<List<Byte>> hashedValuesSet = new HashSet<>();

        LinkedList<LinkedList<Integer>> totalRuntimesByLengthCollision = new LinkedList<>();
        LinkedList<LinkedList<Integer>> totalRunTimesByLengthPreImage = new LinkedList<>();
        LinkedList<Integer> averageRuntimesByLengthCollision = new LinkedList<>();
        LinkedList<Integer> averageRunTimesByLengthPreImage = new LinkedList<>();

        for (int length = 0; length < lengthsToTest; length++)
        {
            LinkedList<Integer> currentLengthResultListCollision = new LinkedList<>();
            LinkedList<Integer> currentLengthResultListPreImage = new LinkedList<>();
            preImageDigest = sha1_1.digestMessage(preImage, digestBitLength[length]);

            for (int round = 0; round < rounds; round++)
            {
                triesToSuccess = 0;
                hashedValuesSet.add(sha1_1.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]));
                boolean hasCollision = false;
                do {
                    triesToSuccess++;
                    result1 = sha1_1.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);

                    if(hashedValuesSet.contains(result1))
                    {
                        hasCollision = true;
                    }
                    else
                    {
                        hashedValuesSet.add(result1);
                    }
                } while (!hasCollision);
                currentLengthResultListCollision.add(triesToSuccess);
                hashedValuesSet.clear();

                triesToSuccess = 0;
                do {
                   triesToSuccess++;
                   result1 = sha1_1.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);
                } while(!preImageDigest.equals(result1));
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
