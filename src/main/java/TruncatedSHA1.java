import org.apache.commons.lang3.ArrayUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * This class is a wrapper for the native Java SHA-1 implementation. It uses this implementation to return a truncated message digest. <b>Do not use for anything other than experimentation. SHA-1 is considered insecure even before truncating the digest.</b>
 */
public class TruncatedSHA1
{
    /**
     * This message creates a truncated SHA-1 digest of a given message.
     * @param message Message to create SHA-1 hash for
     * @param digestBitLength Number of bits to make the digest. Full length of normal SHA-1 digest is 160 bits, so putting in this value will return a fully unaltered SHA-1 digest. <b>Do not input values greater than 160</b>
     * @return The SHA-1 message digest, truncated to the specified number of bits. If the bit count does not fit evenly into bytes, the trailing bits in the last byte will be 0. <b>This output is not cryptographically secure</b>
     */
    public List<Byte> digestMessage(String message, int digestBitLength)
    {
        try
        {
            java.security.MessageDigest sha1 = java.security.MessageDigest.getInstance("SHA-1");

            sha1.reset();
            sha1.update(message.getBytes());

            byte[] fullResult = sha1.digest();

            List<Byte> result = Arrays.asList(ArrayUtils.toObject(fullResult));

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
