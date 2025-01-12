package lms.io;

import lms.exceptions.FileFormatException;
import lms.grid.Coordinate;
import lms.grid.GameGrid;
import lms.grid.GridComponent;
import lms.logistics.Item;
import lms.logistics.Path;
import lms.logistics.belts.Belt;
import lms.logistics.container.Producer;
import lms.logistics.container.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * This class is responsible for loading (reading and parsing) a text file containing details
 * required for the creation of a simulated factory represented in the form of a graphical hexagonal
 * grid. The factory consists of hexagonal nodes (as seen in a beehive) which are linked together to
 * form a complete and symmetrical grid. Each node within this grid provides a depiction of one or
 * more simulated production line(s) nodes. A production line consists of one Producer, one or more
 * Receiver(s) and numerous connected nodes, called Belts.
 *
 * The Producer nodes produce Items while the Receiver Nodes consume them. In between each pair
 * (or more) of a Producer and Receiver, are conveyor belt nodes. Each belt node transports the
 * Items produced by the Producer towards the direction of the connected Receiver(s). Each
 * production line, can have one or more Producer, and one or more Receiver.
 */
public class GameLoader {

    /**
     * Constructor
     */
    public GameLoader() {}

    /**
     * The load method provides an access point to load and parse the grid map text file.
     *
     * @param reader the reader to read from
     * @return the game grid loaded from the reader file
     * @throws IOException if there is an error reading from the reader
     * @throws FileFormatException if the file is not in the correct format
     */
    public static GameGrid load(Reader reader) throws IOException, FileFormatException {
        GameGrid gameGrid;
        List<Object> elements = new ArrayList<>();
        List<Path> elementsPath = new ArrayList<>();
        List<String> producerKey;
        List<String> receiverKey;
        int numProducer;
        int numReceiver;
        int range;
        BufferedReader newReader;

        if (reader == null) {
            throw new NullPointerException();
        }

        newReader = new BufferedReader(reader);

        try {
            String line = newReader.readLine();

            if (line == null) {
                throw new FileFormatException();
            }
            /*Gets range*/
            try {
                range = Integer.parseInt(line);
                if (range < 1) {
                    throw new FileFormatException();
                }
            } catch (NumberFormatException e) {
                throw new FileFormatException();
            }
            gameGrid = new GameGrid(range);
            checkSplit(newReader);
            /*gets number of producers*/
            line = newReader.readLine();
            numProducer = Integer.parseInt(line);
            producerKey = new ArrayList<>(numProducer);
            /*gets number of receivers*/
            line = newReader.readLine();
            numReceiver = Integer.parseInt(line);
            receiverKey = new ArrayList<>(numReceiver);
            checkSplit(newReader);
            /*gets producer key id*/

            for (int i = 0; i < numProducer; i++) {
                line = newReader.readLine();
                producerKey.add(line);
            }
            checkSplit(newReader);
            /*gets receiver key id*/

            for (int i = 0; i < numReceiver; i++) {
                line = newReader.readLine();
                receiverKey.add(line);
            }

            checkSplit(newReader);
            /*gets grid tiles*/
            putTiles(range, newReader, gameGrid, producerKey, receiverKey, numProducer, numReceiver,
                    elements, elementsPath);
            checkSplit(newReader);
            /*gets Linking data*/
            line = newReader.readLine();
            linkingData(line, elements, newReader, elementsPath);

        } catch (IOException e) {
            throw new IOException();
        }

        return gameGrid;
    }


    /**
     * Helper Method:
     * Gets encoding and sets them on the grid
     * @param range range of the grid
     * @param newReader Buffered reader
     * @param gameGrid GameGrid to be initialized
     * @param producerKey list of producer keys
     * @param receiverKey list of receiver keys
     * @param numProducer number of producer on grid
     * @param numReceiver number of receiver on grid
     * @param elements grid components that are on grid
     * @param elementsPath path of the grid components that are on grid
     * @throws IOException if there is an error reading from the reader
     * @throws FileFormatException if format of file is wrong
     */
    private static void putTiles(int range, BufferedReader newReader, GameGrid gameGrid,
                                 List<String> producerKey, List<String> receiverKey,
                                 int numProducer, int numReceiver, List<Object> elements,
                                 List<Path> elementsPath) throws IOException, FileFormatException {
        int column = 1;
        int producerIndex = 0;
        int receiverIndex = 0;
        int xcord;
        int required = range;
        String line;
        int totalLength = range * 2 + 1;
        for (int i = 0; i < totalLength; i++) {
            line = newReader.readLine();
            String[] encoding = line.split(" ");
            // Remove empty strings from the array
            encoding = Arrays.stream(encoding)
                    .filter(str -> !str.isEmpty())
                    .toArray(String[]::new);
            int buffer = 0;
            if (i > range) {
                xcord = -range;
                required--;
            } else if (i == range) {
                xcord = -range;
                required++;
            } else {
                xcord = -i;
                required++;
            }
            if (encoding.length != required) {
                throw new FileFormatException();
            }

            for (String s : encoding) {
                switch (s) {
                    case "p" -> {
                        try {
                            Item key = new Item(producerKey.get(producerIndex));
                            Producer toGrid = new Producer(column, key);
                            elements.add(toGrid);
                            elementsPath.add(toGrid.getPath());
                            gameGrid.setCoordinate(new Coordinate(xcord + buffer,
                                            -range + i), toGrid);
                            producerIndex++;
                            buffer++;
                            column++;
                        } catch (IndexOutOfBoundsException e) {
                            throw new FileFormatException();
                        }

                    }
                    case "r" -> {
                        try {
                            Item key = new Item(receiverKey.get(receiverIndex));
                            Receiver toGrid = new Receiver(column, key);
                            elements.add(toGrid);
                            elementsPath.add(toGrid.getPath());
                            gameGrid.setCoordinate(new Coordinate(xcord + buffer,
                                            -range + i), toGrid);
                            receiverIndex++;
                            buffer++;
                            column++;
                        } catch (IndexOutOfBoundsException e) {
                            throw new FileFormatException();
                        }
                    }
                    case "b" -> {
                        Belt toGrid = new Belt(column);
                        elements.add(toGrid);
                        elementsPath.add(toGrid.getPath());
                        gameGrid.setCoordinate(new Coordinate(xcord + buffer, -range + i),
                                toGrid);
                        buffer++;
                        column++;
                    }
                    case "o" -> {
                        gameGrid.setCoordinate(new Coordinate(xcord + buffer, -range + i),
                            () -> "o");
                        buffer++;
                    }
                    case "w" -> {
                        gameGrid.setCoordinate(new Coordinate(xcord + buffer, -range + i),
                            () -> "w");
                        buffer++;
                    }
                    default -> throw new FileFormatException();
                }

            }
        }
        if (producerIndex < producerKey.size() || receiverIndex < receiverKey.size()
                || numProducer != producerKey.size() || numReceiver != receiverKey.size()) {
            throw new FileFormatException();
        }
    }

    /**
     * Helper Method:
     * sets in put and out put nodes for the given nodes
     *
     * @param line the extracted line from text
     * @param elements  grid components that are on grid
     * @param newReader Buffered reader
     * @param elementsPath path of the grid components that are on grid
     * @throws IOException if there is an error reading from the reader
     * @throws FileFormatException if format of file is wrong
     */
    private static void linkingData(String line, List<Object> elements, BufferedReader newReader,
                                    List<Path> elementsPath)
            throws IOException, FileFormatException {
        while (!(line == null)) {
            try {
                String[] beltNext = line.split(",");
                String[] current = beltNext[0].split("-");

                if (beltNext.length > 2 || current.length > 2) {
                    throw new FileFormatException();
                }

                if (beltNext.length == 2 && !(beltNext[1].isEmpty()) && elements
                        .get(Integer.parseInt(current[0]) - 1).getClass() != Belt.class) {
                    throw new FileFormatException();

                } else if (beltNext.length == 2 && !(beltNext[1].isEmpty())) {
                    ((Belt) elements.get(Integer.parseInt(current[0]) - 1)).setOutput(
                            elementsPath.get(Integer.parseInt(beltNext[1]) - 1));
                    /*ViceVersa of the given*/
                    beltConnectionHelper(beltNext, current, elements, elementsPath);
                }

                if (current.length == 2 && elements
                        .get(Integer.parseInt(current[0]) - 1).getClass() == Belt.class) {

                    ((Belt) elements.get(Integer.parseInt(current[0]) - 1)).setInput(
                        elementsPath.get(Integer.parseInt(current[1]) - 1));
                    /*ViceVersa of the given*/
                    beltReceiverHelper(current, elements, elementsPath);

                } else if (current.length == 2 && elements
                        .get(Integer.parseInt(current[0]) - 1).getClass() == Receiver.class) {

                    ((Receiver) elements.get(Integer.parseInt(current[0]) - 1)).setInput(
                            elementsPath.get(Integer.parseInt(current[1]) - 1));
                    /*ViceVersa of the given*/
                    beltReceiverHelper(current, elements, elementsPath);

                } else if (current.length == 2 && elements
                        .get(Integer.parseInt(current[0]) - 1).getClass() == Producer.class) {

                    ((Producer) elements.get(Integer.parseInt(current[0]) - 1)).setOutput(
                            elementsPath.get(Integer.parseInt(current[1]) - 1));
                    /*ViceVersa of the given*/
                    beltConnectionHelper(current, current, elements, elementsPath);
                }

                line = newReader.readLine();
            } catch (IndexOutOfBoundsException e) {
                throw new FileFormatException();
            }
        }
        /*Do pathKeys check here*/
        pathKeys(elements);
    }

    /**
     * Helper Method:
     * Checks to see if splitter is there
     *
     * @param newReader Buffered reader
     * @throws IOException if there is an error reading from the reader
     * @throws FileFormatException if no splitter exists
     */
    private static void checkSplit(BufferedReader newReader)
            throws IOException, FileFormatException {
        String line;
        line = newReader.readLine();
        if (! line.startsWith("_____")) {
            throw new FileFormatException();
        }
    }

    /**
     * Helper Method:
     * Adds the reciprocal of the belt connections
     *
     * @param current first part of the text line
     * @param elements grid components that are on grid
     * @param elementsPath path of the grid components that are on grid
     */
    private static void beltReceiverHelper(String[] current, List<Object> elements,
                                           List<Path> elementsPath) {
        if (elements.get(Integer.parseInt(current[1]) - 1).getClass() == Belt.class) {
            ((Belt) elements.get(Integer.parseInt(current[1]) - 1)).setOutput(
                    elementsPath.get(Integer.parseInt(current[0]) - 1));

        } else if (elements
                .get(Integer.parseInt(current[1]) - 1).getClass() == Producer.class) {
            ((Producer) elements.get(Integer.parseInt(current[1]) - 1)).setOutput(
                    elementsPath.get(Integer.parseInt(current[0]) - 1));
        }
    }

    /**
     * Helper Method:
     * Adds the reciprocal of the belt connections
     *
     * @param beltNext second part of the text line
     * @param current first part of the text line
     * @param elements grid components that are on grid
     * @param elementsPath path of the grid components that are on grid
     */
    private static void beltConnectionHelper(String[] beltNext, String[] current,
                                             List<Object> elements, List<Path> elementsPath) {
        if (elements.get(Integer.parseInt(beltNext[1]) - 1).getClass() == Belt.class) {
            ((Belt) elements.get(Integer.parseInt(beltNext[1]) - 1)).setInput(
                    elementsPath.get(Integer.parseInt(current[0]) - 1));

        } else if (elements
                .get(Integer.parseInt(beltNext[1]) - 1).getClass() == Receiver.class) {
            ((Receiver) elements.get(Integer.parseInt(beltNext[1]) - 1)).setInput(
                    elementsPath.get(Integer.parseInt(current[0]) - 1));
        }
    }

    /**
     * Helper Method:
     * Checks to see if all producer keys match with receiver keys
     *
     * @param elements grid components that are on grid
     * @throws FileFormatException When producer and receiver keys don't match
     */
    private static void pathKeys(List<Object> elements) throws FileFormatException {
        for (Object element : elements) {
            if (element.getClass() == Receiver.class) {
                if (((Receiver) element).getPath().head().getNode() instanceof Producer) {
                    if (!Objects.equals(((Receiver) element).getKey(), ((Producer) (((Receiver)
                            element).getPath().head().getNode())).getKey())) {
                        throw new FileFormatException();
                    }
                }
                if (!(((Receiver) element).getPath().head().getNode() instanceof Producer)
                        || !(((Receiver) element).getPath().tail().getNode() instanceof Receiver)) {
                    throw new FileFormatException();
                }
            }
            if (element.getClass() == Belt.class) {
                if (!(((Belt) element).getPath().head().getNode() instanceof Producer)
                        || !(((Belt) element).getPath().tail().getNode() instanceof Receiver)) {
                    throw new FileFormatException();
                }
            }
            if (element.getClass() == Producer.class) {
                if (!(((Producer) element).getPath().head().getNode() instanceof Producer)
                        || !(((Producer) element).getPath().tail().getNode() instanceof Receiver)) {
                    throw new FileFormatException();
                }
            }
        }
    }
}