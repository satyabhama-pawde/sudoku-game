import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuSolverGUI extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];

    // Partially filled puzzle  
    private int[][] puzzle = {
            {5, 3, 0, 6, 7, 8, 0, 0, 2},
            {6, 0, 2, 1, 9, 5, 0, 4, 8},
            {1, 9, 8, 3, 4, 0, 5, 6, 7},

            {8, 5, 9, 0, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 0, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 0, 8, 5, 6},

            {0, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 0, 5},
            {0, 4, 5, 2, 8, 0, 1, 7, 0}
    };

    private int[][] solution = new int[9][9];

    public SudokuSolverGUI() {
        setTitle("Sudoku Solver - Live Color Validation");
        setSize(650, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Copy puzzle -> solution and solve  
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                solution[r][c] = puzzle[r][c];
        solve(solution);

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        Color block1 = new Color(225, 235, 255);
        Color block2 = new Color(255, 235, 225);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 32));
                cell.setBorder(new LineBorder(Color.BLACK, 1));

                if (((r / 3) + (c / 3)) % 2 == 0)
                    cell.setBackground(block1);
                else
                    cell.setBackground(block2);

                if (puzzle[r][c] != 0) {
                    cell.setText(String.valueOf(puzzle[r][c]));
                    cell.setEditable(false);
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setForeground(Color.BLACK);

                    final int row = r, col = c;

                    cell.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            String text = cell.getText().trim();

                            if (!text.matches("[1-9]?")) {
                                cell.setText("");
                                cell.setForeground(Color.BLACK);
                                return;
                            }

                            if (text.isEmpty()) {
                                cell.setForeground(Color.BLACK);
                                return;
                            }

                            int value = Integer.parseInt(text);

                            if (value == solution[row][col]) {
                                cell.setForeground(Color.GREEN);

                                
                                // CHECK IF PUZZLE IS COMPLETED
                                
                                if (isWinner()) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "🎉 You are the winner! Sudoku Completed!"
                                    );
                                }

                            } else {
                                cell.setForeground(Color.RED);
                            }
                        }
                    });
                }

                cells[r][c] = cell;
                gridPanel.add(cell);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // Buttons  
        JPanel bottomPanel = new JPanel();

        JButton solveBtn = new JButton("Solve");
        solveBtn.setFont(new Font("Arial", Font.BOLD, 22));
        solveBtn.setBackground(new Color(0, 180, 120));
        solveBtn.setForeground(Color.WHITE);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Arial", Font.BOLD, 22));
        clearBtn.setBackground(new Color(200, 60, 60));
        clearBtn.setForeground(Color.WHITE);

        bottomPanel.add(solveBtn);
        bottomPanel.add(clearBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Solve button  
        solveBtn.addActionListener(e -> {
            for (int r = 0; r < 9; r++)
                for (int c = 0; c < 9; c++)
                    cells[r][c].setText(String.valueOf(solution[r][c]));
        });

        // Clear button  
        clearBtn.addActionListener(e -> {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (puzzle[r][c] == 0) {
                        cells[r][c].setText("");
                        cells[r][c].setForeground(Color.BLACK);
                    }
                }
            }
        });
    }

    // WINNER CHECK FUNCTION
    private boolean isWinner() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++) {
                String text = cells[r][c].getText().trim();
                if (!text.matches("[1-9]")) return false;
                if (Integer.parseInt(text) != solution[r][c]) return false;
            }
        return true;
    }

    // Solver  
    private boolean solve(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, r, c, num)) {
                            board[r][c] = num;
                            if (solve(board)) return true;
                            board[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) return false;
            if (board[i][col] == num) return false;
        }

        int br = row - row % 3, bc = col - col % 3;

        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[br + r][bc + c] == num)
                    return false;

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuSolverGUI().setVisible(true));
    }

}

