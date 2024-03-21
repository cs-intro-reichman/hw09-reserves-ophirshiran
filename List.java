/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;

    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    public static void main(String[] args) {
        List q = new List();
        q.update('_');
        q.update('e');
        q.update('e');
        q.update('t');
        q.update('t');
        q.update('i');
        q.update('m');
        q.update('m');
        q.update('o');
        q.update('c');
        System.out.println(q);
       /* q.addFirst('a');
        q.addFirst('b');
        q.addFirst('c');
        System.out.println(q);
        System.out.println(q.indexOf('c'));
        q.remove('b');
        System.out.println(q);
        q.addFirst('o');
        q.addFirst('f');
        System.out.println(q);
        System.out.println(q.get(2));
        System.out.println(q);
        //System.out.println(q.get(5));
        System.out.println(q.remove('t'));
        q.update('f');
        System.out.println(q);
        q.update('t');
        System.out.println(q); */


    }

    /** Returns the number of elements in this list. */
    public int getSize() {
        return size;
    }
    public Node getFirstNode()
    {
        return first;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {

        CharData newCharData = new CharData(chr);
        // Create a new Node with the newly created CharData object
        Node newNode = new Node(newCharData, this.first);
        // Update the first node to the newly created node
        this.first = newNode;
        // Increment the size of the list
        size++;
    }

    /** GIVE Textual representation of this list. */
    public String toString() {
        if (size == 0)
        {
            return "()";
        }
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null)
        {
            str.append(current.cp.toString()).append(" ");
            current = current.next;
        }
        //removes the trailing space and adds the ')'
        str.deleteCharAt(str.length() - 1).append(")");
        return str.toString();
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null)
        {
            if (current.cp.chr == chr)
            {
                return index;
            }
            current = current.next;
            index ++;
        }
        return -1; //value not found

    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) //we managed to find the char
        {
            Node current = first;
            while (current != null)
            {
                if (current.cp.chr == chr)
                {
                    current.cp.count++;//increments the counter
                }
                current = current.next;
            }
        }
        else
        {
            addFirst(chr);
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        //finds the node to renmove using to poninters, prev is one step behind
        Node prev = null;
        Node current = first;
        while (current != null && current.cp.chr != chr)
        {
            prev = current;
            current = current.next;
        }
        if (current == null)
        {
            return false;
        }
        if (prev == null) //its the first element
        {
            first = first.next;
        }
        else
        {
            prev.next = current.next; //one behind points one ahead
        }
        size --; //updates the lists size
        return true;
        
    }

    /** Returns the CharData object at the specified index in this list.
     *  If the index is negative or is greater than the size of this list,
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException("Index is out of bounds: " + index);
        }
        int i = 0;
        Node current = first;
        while (current != null)
        {
            if (i == index)
            {
                return current.cp;
            }
            current = current.next;
            i ++;
        }
        return null; //if somehow there wasnt an index out of bounds exception or a Chardata return
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++]  = current.cp;
            current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
        // If the list is empty, there is nothing to iterate
        if (size == 0) return null;
        // Gets the element in position index of this list
        Node current = first;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
        return new ListIterator(current);
    }
}