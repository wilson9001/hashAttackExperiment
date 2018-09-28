import java.util.ArrayList;
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
        LinkedList<Integer> averageRuntimesByLengthCollision = new LinkedList<>();

        for (int length = 0; length < lengthsToTest; length++)
        {
            //totalRuntimesByLengthCollision.add(new LinkedList<>());
            LinkedList<Integer> currentLengthResultList = new LinkedList<>();

            for (int round = 0; round < rounds; round++)
            {
                triesToSuccess = 0;
                do {
                    triesToSuccess++;
                    result1 = sha1_1.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);
                    result2 = sha1_2.digestMessage(UUID.randomUUID().toString(), digestBitLength[length]);

                    //System.out.println("Truncated array:");
                    //printBytesAsHex(result1);
                    //printBytesAsHex(result2);
                } while (!result1.equals(result2));
                currentLengthResultList.add(triesToSuccess);
                //String resultMessage = "Collision found on %d bit digest after %d attempts";
                //resultMessage = String.format(resultMessage, digestBitLength[length], triesToSuccess);

                //System.out.println(resultMessage);


            }
            totalRuntimesByLengthCollision.add(currentLengthResultList);
            long totalTries = 0;
            for(int attempt : currentLengthResultList)
            {
                totalTries += attempt;
            }

            averageRuntimesByLengthCollision.add((int)(totalTries / rounds));
        }

        for(int length = 0; length < lengthsToTest; length++)
        {
            System.out.print("Average Collision Try for key length ");
            System.out.println(digestBitLength[length]);
            System.out.println(averageRuntimesByLengthCollision.get(length));
            System.out.println("Actual Collision tries are:");
            List<Integer> LengthResults = totalRuntimesByLengthCollision.get(length);

            for(Integer result : LengthResults)
            {
                System.out.println(result);
            }
        }
    }

    /**
     * This helper function prints a list of bytes as a single continuous hex string.
     * The output is of the format '0x{...}', with the entire array of bytes as hex following.
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
