package lms.logistics;

import lms.logistics.container.Producer;
import lms.logistics.container.Receiver;

import java.util.function.Consumer;

/**
 * Maintains a doubly linked list to maintain the links for each node.
 * Has previous and next item.
 * The path can't have an empty node, as it will throw an illegal
 * argument exception.
 * @version 1.0
 * @ass2
 */
public class Path {

    /**
     * Transport node for this Path.
     */
    private Transport node;

    /**
     * Previous Path node for this Transport.
     */
    private Path previous;

    /**
     * Next Path node for this Transport.
     */
    private Path next;



    /**
     * This method takes a Transport Consumer,
     * using the Consumer&lt;T&gt; functional interface from java.util.
     * It finds the tail of the path and calls
     * Consumer&lt;T&gt;'s accept() method with the tail node as an argument.
     * Then it traverses the Path until the head is reached,
     * calling accept() on all nodes.
     *
     * This is how we call the tick method for all the different transport items.
     *
     * @param consumer Consumer&lt;Transport&gt;
     * @see java.util.function.Consumer
     * @provided
     */
    public void applyAll(Consumer<Transport> consumer) {
        Path path = tail(); // IMPORTANT: go backwards to aid tick
        do {
            consumer.accept(path.node);
            path = path.previous;
        } while (path != null);
    }

    /**
     * Constructs a new Path object with the same Transport node, previous Path,
     * and next Path as the specified Path object.
     *
     * @param path the Path object to copy.
     * @throws IllegalArgumentException if the path argument is null.
     */
    public Path(Path path) throws IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        this.node = path.getNode();
        this.next = path.getNext();
        this.previous = path.getPrevious();
    }

    /**
     * Constructs a new Path object with the specified Transport node, and sets the previous and
     * next Path objects in the path to null. Throws an IllegalArgumentException if the node
     * argument is null.
     *
     * @param node the Transport node for this Path.
     * @throws IllegalArgumentException if the path argument is null.
     */
    public Path(Transport node) throws IllegalArgumentException {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.node = node;
        this.next = null;
        this.previous = null;
    }

    /**
     * Constructs a new Path object with the specified Transport node, and the previous and next
     * Path objects in the path. Throws an IllegalArgumentException if the node argument is null.
     *
     * @param node the Transport node for this Path.
     * @param previous the previous Path object in the path.
     * @param next the next Path object in the path.
     * @throws IllegalArgumentException if the node argument is null.
     */
    public Path(Transport node, Path previous, Path next) throws IllegalArgumentException {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.node = node;
        this.next = next;
        this.previous = previous;
    }

    /**
     * Accessor method for the transport node associated with this path.
     *
     * @return the transport node associated with this path
     */
    public Transport getNode() {
        return this.node;
    }

    /**
     * Returns the tail of this Path, which is the last element in the path.
     * If this Path is the last element, it is returned as is.
     *
     * @return the tail of this Path.
     */
    public Path tail() {
        Path forward = this;
        while (forward.next != null) {
            forward = forward.next;
        }
        return forward;
    }

    /**
     * Returns the head of this Path, which is the first element in the path.
     * If this Path is the first element, it is returned as is.
     *
     * @return the head of this Path.
     */
    public Path head() {
        Path before = this;
        while (before.previous != null) {
            before = before.previous;
        }
        return before;
    }

    /**
     * Returns the previous Path object in the chain.
     *
     * @return the previous Path object in the chain, or null if this is the first Path object
     */
    public Path getPrevious() {
        if (this.equals(this.head())) {
            return null;
        }
        return this.previous;
    }

    /**
     * Returns the next Path object in the chain.
     *
     * @return the next Path object in the chain, or null if this is the last Path object
     */
    public Path getNext() {
        if (this.equals(this.tail())) {
            return null;
        }
        return this.next;
    }

    /**
     * Sets the next path for this path.
     *
     * @param path the next path to be set for this path
     */
    public void setNext(Path path) {
        if (!(this.node instanceof Receiver)) {
            this.next = path;
        }

    }

    /**
     * Sets the previous path for this path.
     *
     * @param path the previous path to be set for this path
     *
     */
    public void setPrevious(Path path) {
        if (!(this.node instanceof Producer)) {
            this.previous = path;
        }
    }

    /**
     * toString that provides a list of Path nodes from a Producer, along the belt to a Receiver.
     *
     * @return String representing the entirety of the best path links
     */
    @Override
    public String toString() {
        StringBuilder pathList = new StringBuilder("START -> ");
        Path path = this.head();
        while (path != null) {
            pathList.append(path.getNode().toString()).append(" -> ");
            path = path.getNext();
        }
        pathList.append("END");
        return pathList.toString();
    }


    /**
     * Compares this Path object to the specified object for equality. Returns true if and only if
     * the specified object is also a Path object and has the same Transport node as this Path
     * object.
     *
     * @param o the object to compare this Path against
     * @return true if the specified object is equal to this Path object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (Path.class == o.getClass()) {
            return ((Path) o).getNode() == this.node;
        }
        return false;
    }
}

