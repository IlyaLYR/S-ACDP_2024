package ru.vsu.cs;

import ru.vsu.cs.task3.PriorityQueue;
import ru.vsu.cs.task3.PriorityQueueMaxHeap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random rand = new Random();

        PriorityQueue<Integer> pq1 = new PriorityQueue<>();
        PriorityQueueMaxHeap<Integer> pq2 = new PriorityQueueMaxHeap<>();

        int[] x = new int[100];
        for (int i = 0; i < x.length; i += 1) {
            x[i] = (i + 1) * 100;
        }
        double[] y = new double[100];
        // Тест запись
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < x.length; i++) {
                for (int k = 0; k < 100; k++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);
                    pq1.insert(val, pr);
                }

                for (int e = 0; e < 10; e++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);

                    pq1.insert(val, pr);
                    pq1.extractMax();
                }
            }
        }
        pq1.clean();

        for (int i = 0; i < x.length; i++) {
            for (int k = 0; k < 100; k++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);
                pq1.insert(val, pr);
            }

            double[] avg = new double[100];
            for (int e = 0; e < 100; e++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);

                double start = System.nanoTime();
                pq1.insert(val, pr);
                double end = System.nanoTime();
                pq1.extractMax();
                avg[e] = end - start;
            }
            double resul = 0;
            for (double s : avg) {
                resul += s;
            }
            y[i] = resul / 100;
        }


        try {
            FileWriter writer = new FileWriter("example1.xls");
            for (int i = 0; i < x.length; i++) {
                writer.write(x[i] + " " + y[i] + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Тест извлечения
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < x.length; i++) {
                for (int k = 0; k < 100; k++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);
                    pq1.insert(val, pr);
                }

                for (int e = 0; e < 100; e++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);

                    pq1.extractMax();
                    pq1.insert(val, pr);
                }
            }
        }
        pq2.clean();

        for (int i = 0; i < x.length; i++) {
            for (int k = 0; k < 100; k++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);
                pq1.insert(val, pr);
            }

            double[] avg = new double[100];
            for (int e = 0; e < 100; e++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);

                double start = System.nanoTime();
                pq1.extractMax();
                double end = System.nanoTime();
                pq1.insert(val, pr);
                avg[e] = end - start;
            }
            double resul = 0;
            for (double s : avg) {
                resul += s;
            }
            y[i] = resul / 100;
        }

        try {
            FileWriter writer = new FileWriter("example2.xls");
            for (int i = 0; i < x.length; i++) {
                writer.write(x[i] + " " + y[i] + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Тест запись
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < x.length; i++) {
                for (int k = 0; k < 100; k++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);
                    pq2.insert(val, pr);
                }

                for (int e = 0; e < 10; e++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);

                    pq2.insert(val, pr);
                    pq2.extractMax();
                }
            }
        }

        pq2.clean();

        for (int i = 0; i < x.length; i++) {
            for (int k = 0; k < 100; k++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);
                pq2.insert(val, pr);
            }

            double[] avg = new double[100];
            for (int e = 0; e < 100; e++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);

                double start = System.nanoTime();
                pq2.insert(val, pr);
                double end = System.nanoTime();
                pq2.extractMax();
                avg[e] = end - start;
            }
            double resul = 0;
            for (double s : avg) {
                resul += s;
            }
            y[i] = resul / 100;
        }


        try {
            FileWriter writer = new FileWriter("example3.xls");
            for (int i = 0; i < x.length; i++) {
                writer.write(x[i] + " " + y[i] + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Тест извлечения
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < x.length; i++) {
                for (int k = 0; k < 100; k++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);
                    pq2.insert(val, pr);
                }

                for (int e = 0; e < 100; e++) {
                    int val = rand.nextInt(100);
                    int pr = rand.nextInt(10);

                    pq2.insert(val, pr);
                    pq2.extractMax();
                }
            }
        }
        pq2.clean();

        for (int i = 0; i < x.length; i++) {
            for (int k = 0; k < 100; k++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);
                pq2.insert(val, pr);
            }

            double[] avg = new double[100];
            for (int e = 0; e < 100; e++) {
                int val = rand.nextInt(100);
                int pr = rand.nextInt(10);

                double start = System.nanoTime();
                pq2.extractMax();
                double end = System.nanoTime();
                pq2.insert(val, pr);
                avg[e] = end - start;
            }
            double resul = 0;
            for (double s : avg) {
                resul += s;
            }
            y[i] = resul / 100;
        }

        try {
            FileWriter writer = new FileWriter("example4.xls");
            for (int i = 0; i < x.length; i++) {
                writer.write(x[i] + " " + y[i] + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
