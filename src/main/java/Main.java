import java.util.List;

public class Main
{
    public static void main(String arg[])
    {
        TruncatedSHA1 sha1 = new TruncatedSHA1();

        List<Byte> result = sha1.digestMessage("Hello", 5);

        if (result == null)
        {
            System.out.println("Failed to initialize TruncatedSHA1 instance");
        }
        else
        {
            System.out.println("Truncated array:");
            System.out.println(result);
        }
    }
}
