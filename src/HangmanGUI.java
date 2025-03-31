import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HangmanGUI {
    private JPanel gamePanel;
    private JButton prevBtn;
    private JButton nextBtn;
    private JLabel hangmanPic;
    private JList<String> letterList;
    private JLabel letterCount;
    private JTextField inputField;
    private JLabel inputLabel;

    private int countdown = 1;
    private String currentWord;
    private StringBuilder displayWord;
    private List<String> words;
    private DefaultListModel<String> letterListModel;  // Model for letterList

    public HangmanGUI() {
        this.words = new ArrayList<>();
        this.letterListModel = new DefaultListModel<>();
        this.letterList.setModel(letterListModel);  // Attach model to letterList

        // Adding words to the list
        add("apple");
        add("pear");
        add("cherry");
        add("mango");
        add("watermelon");
        add("strawberry");
        add("kiwi");

        this.prevBtn.addActionListener(new ImageChangeBtnClick(-1));
        this.nextBtn.addActionListener(new ImageChangeBtnClick(1));

        ImageIcon new_picture = new ImageIcon("hangman/hangman" + countdown + ".png");
        Image image = new_picture.getImage();
        Image newimg = image.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        new_picture = new ImageIcon(newimg);
        hangmanPic.setIcon(new_picture);

        selectRandomWord();
        updateDisplayWord();

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText().toLowerCase();
                if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                    if (!isLetterAlreadyGuessed(input)) {  // Check for duplicates
                        letterListModel.addElement(input);  // Add guessed letter to the list
                        guess(input.charAt(0));
                    }
                }
                inputField.setText("");
            }
            private boolean isLetterAlreadyGuessed(String letter) {
                for (int i = 0; i < letterListModel.size(); i++) {
                    if (letterListModel.getElementAt(i).equals(letter)) {
                        return true;  // The letter is already in the list
                    }
                }
                return false;  // The letter is not in the list
            }
        });
    }

    public void add(String word) {
        words.add(word.toLowerCase());
    }

    private void selectRandomWord() {
        Random obj = new Random();
        currentWord = words.get(obj.nextInt(words.size()));
        displayWord = new StringBuilder("_".repeat(currentWord.length()));
    }

    private void updateDisplayWord() {
        inputLabel.setText(displayWord.toString().replace("", " ").trim());
    }

    private void guess(char letter) {
        boolean found = false;
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == letter) {
                displayWord.setCharAt(i, letter);
                found = true;
            }
        }

        if (!found) {
            countdown++;
            if (countdown > 10) countdown = 10;

            ImageIcon new_picture = new ImageIcon("hangman/hangman" + countdown + ".png");
            Image image = new_picture.getImage();
            Image newimg = image.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            new_picture = new ImageIcon(newimg);
            hangmanPic.setIcon(new_picture);

            // Check if game is over
            if (countdown == 10) {  // Game over condition
                JOptionPane.showMessageDialog(gamePanel, "You lose! The word was: " + currentWord);
                return;
            }
        }

        updateDisplayWord();

        if (displayWord.toString().equals(currentWord)) {
            JOptionPane.showMessageDialog(gamePanel, "You guessed the word!");
        }
    }

    private class ImageChangeBtnClick implements ActionListener {
        private int direction;

        public ImageChangeBtnClick(int val) {
            this.direction = val;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            countdown += direction;

            if (countdown < 1) countdown = 10;
            else if (countdown > 10) countdown = 1;

            ImageIcon new_picture = new ImageIcon("hangman/hangman" + countdown + ".png");
            Image image = new_picture.getImage();
            Image newimg = image.getScaledInstance(250, 250,  java.awt.Image.SCALE_SMOOTH);
            new_picture = new ImageIcon(newimg);
            hangmanPic.setIcon(new_picture);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hangman Game");
        frame.setContentPane(new HangmanGUI().gamePanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
