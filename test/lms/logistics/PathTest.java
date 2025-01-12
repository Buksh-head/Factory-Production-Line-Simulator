package lms.logistics;

import lms.exceptions.FileFormatException;
import lms.logistics.belts.Belt;
import lms.logistics.container.Producer;
import lms.logistics.container.Receiver;
import org.junit.*;

import static org.junit.Assert.*;

public class PathTest {
    public Belt belt1;
    public Belt belt2;
    public Receiver receiver1;
    public Producer producer1;
    public Path path1;
    public Path path2;
    public Path path3;
    public Path path4;
    public Path path5;
    public Path pathP;
    public Path pathR;
    @Before
    public void setUp() throws Exception {
        belt1 = new Belt(1);
        receiver1 = new Receiver(2,new Item("aa"));
        producer1 = new Producer(3,new Item("aa"));
        belt2 = new Belt(4);
        path1 = new Path(belt1);
        path2 = new Path(producer1,null,path1);
        path3 = new Path(receiver1,path1,null);
        path4 = new Path(belt2);
        pathP = new Path(producer1,null,null);
        pathR = new Path(receiver1,null,null);
        path1.setNext(path3);
        path1.setPrevious(path2);

    }

    @Test
    public void getNode() {
        assertEquals(path3.getNode(),receiver1);
    }

    @Test
    public void tail() {
        assertEquals(path1.tail().getNode(),receiver1);
    }

    @Test
    public void tailP() {
        assertEquals(pathP.tail().getNode(),producer1);
    }

    @Test
    public void tailR() {
        assertEquals(pathR.tail().getNode(),receiver1);
    }

    @Test
    public void head() {
        assertEquals(path1.head().getNode(),producer1);
    }
    @Test
    public void headP() {
        assertEquals(pathP.head().getNode(),producer1);
    }

    @Test
    public void headR() {
        assertEquals(pathR.head().getNode(),receiver1);
    }

    @Test
    public void getPrevious() {
        assertEquals(path3.getPrevious(),path1);
    }

    @Test
    public void getPreviousProducer() {
        assertNull(path2.getPrevious());
    }
    @Test
    public void getPreviousReceiver() {
        assertNull(pathR.getPrevious());
    }

    @Test
    public void getNext() {
        assertEquals(path2.getNext(),path1);
    }

    @Test
    public void getNextReceiver() {
        assertNull(path3.getNext());
    }
    @Test
    public void getNextProducer() {
        assertNull(pathP.getNext());
    }

    @Test
    public void setNext() {
        path4.setNext(pathR);
        assertEquals(path4.getNext(),pathR);
    }

    @Test
    public void setNextReceiver() {
        pathR.setNext(path4);
        assertNull(pathR.getNext());

    }

    @Test
    public void setPrevious() {
        path4.setPrevious(pathP);
        assertEquals(path4.getPrevious(),pathP);
    }

    @Test
    public void setPreviousProducer() {
        pathP.setNext(path4);
        assertNotEquals(pathP.getPrevious(),path4);
        assertNull(pathP.getPrevious());
    }

    @Test
    public void testToString() {
        assertEquals(path2.toString(),
                "START -> <Producer-3> -> <Belt-1> -> <Receiver-2> -> END");
    }

    @Test
    public void testEqualsFalse() {
        assertFalse(path1.equals(path4));
    }

    @Test
    public void testEquals() {
        assertTrue(path1.equals(path1));
    }

    @Test
    public void testEqualsTrue() {
        path5 = new Path(belt2);
        assertTrue(path4.equals(path5));
    }
}