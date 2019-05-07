package br.pro.hashi.ensino.desagil.projeto1;

import java.util.HashMap;
import java.util.LinkedList;

// Não é permitido mudar nada nessa classe
// exceto o recheio dos três métodos.

public class Translator {
    private final Node root;
    private final HashMap<Character, Node> map;


    // Você deve mudar o recheio deste construtor,
    // de acordo com os requisitos não-funcionais.
    public Translator() {
        this.map = new HashMap<>();

        String morseCodeTree = " etianmsurwdkgohvf l pjbxcyzq  54 3   2       16       7   8 90";
        Node[] nodes = new Node[morseCodeTree.length()];

        for (int i = 0; i < morseCodeTree.length(); i++) {
            char nodeValue = morseCodeTree.charAt(i);
            Node node = new Node(nodeValue);
            nodes[i] = node;
            if (nodeValue != ' ') {
                this.map.put(nodeValue, node);
            }
        }

        this.root = nodes[0];

        for (int i = 0; i < morseCodeTree.length(); i++) {
            if (i >= 1) {
                nodes[i].setParent(nodes[(i-1)/2]);
            }
            if (i <= 30) {
                nodes[i].setLeft(nodes[2*i + 1]);
                nodes[i].setRight(nodes[2*(i + 1)]);
            }
        }

    }


    // Você deve mudar o recheio deste método, de
    // acordo com os requisitos não-funcionais.
    public char morseToChar(String code) {
        Node current = root;
        for (int i = 0; i < code.length(); i++) {
            char letter = code.charAt(i);

            if (letter == '-') {
                current = current.getRight();
            } else if (letter == '.') {
                current = current.getLeft();
            } else {
                return ' ';
            }
        }
        return current.getValue();
    }


    // Você deve mudar o recheio deste método, de
    // acordo com os requisitos não-funcionais.
    public String charToMorse(char c) {
        Node current = map.get(c);
        Node lastNode = map.get(c);
        StringBuilder morse = new StringBuilder();

        while (current != root) {
            assert current != null;
            current = current.getParent();
            if (current.getLeft() == lastNode) {
                morse.insert(0, '.');
            } else if (current.getRight() == lastNode) {
                morse.insert(0, '-');
            }
            assert lastNode != null;
            lastNode = lastNode.getParent();
        }

        return morse.toString();
    }


    // Você deve mudar o recheio deste método, de
    // acordo com os requisitos não-funcionais.
    public LinkedList<String> getCodes() {
        return new LinkedList<>();
    }

}
