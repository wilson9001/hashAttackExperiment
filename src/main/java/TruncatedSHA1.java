import org.apache.commons.lang3.ArrayUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class TruncatedSHA1
{
    public List<Byte> digestMessage(String message, int digestBitLength)
    {
        try
        {
            java.security.MessageDigest sha1 = java.security.MessageDigest.getInstance("SHA-1");

            sha1.reset();
            sha1.update(message.getBytes());

            byte[] fullResult = sha1.digest();

            List<Byte> result = Arrays.asList(ArrayUtils.toObject(fullResult));

            System.out.println("Full SHA-1 digest:");
            System.out.println(result);

            int digestWholeBytes = digestBitLength / 8;
            int bitsLeft = digestBitLength % 8;

            result = result.subList(0, bitsLeft == 0 ? digestWholeBytes : (digestWholeBytes+1));

            if(bitsLeft != 0)
            {
                byte lastBytePrimitive = result.get(digestWholeBytes);

                byte[] bitsToWipe = {0x00, 0x7f, 0x3f, 0x1f, 0x0f, 0x07, 0x03, 0x01};

                lastBytePrimitive |= bitsToWipe[bitsLeft];
                lastBytePrimitive ^= bitsToWipe[bitsLeft];

                result.set(digestWholeBytes, lastBytePrimitive);
            }

            return result;
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();

            return null;
        }
    }


}
