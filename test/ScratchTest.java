import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;
import java.io.IOException;
import java.lang.UnsatisfiedLinkError;
import com.example.pkgjni.Scratch;
import java.util.Properties;

public class ScratchTest {

    /**
     * @brief The base test: load a class.
     *
     * This will load a class, and hence will also load the native library.
     */
    @Test
    public void loadsScratch() {
        Scratch s = new Scratch();
    }

    /**
     * @brief Load an invalid library.  Expect Exception UnsatisfiedLinkError
     *
     * We've created a fake .so file, that should not load correctly in any
     * known linker.
     */
    @Test(expected=UnsatisfiedLinkError.class)
    public void failLoadsScratch() {
        Properties props = System.getProperties();
        props.setProperty("nativeLocations.scratch", "fakesofile.so");
        System.setProperties(props);
    	Scratch s = new Scratch();
    }

    /**
     * @brief Test that we can query the JNI library correctly.
     */
    @Test
    public void testScratchAnswer() {
        Scratch itch = new Scratch();
        assertEquals(1, itch.nativeAnswer());
    }

    /**
     * @brief Run all the tests.
     */
    public static void main(String[] args) {
        JUnitCore.main("ScratchTest");
    }

}
