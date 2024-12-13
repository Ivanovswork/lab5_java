class MaxThread extends Thread {
    private int start, end;
    private int[] arr;
    private static int[] maxArr;
    private static int threadCount = 0;

    public MaxThread(int[] arr, int start, int end) {
        this.start = start;
        this.end = end;
        this.arr = arr;
    }

    @Override
    public void run() {
        int maxInSegment = arr[start];
        for (int i = start; i < end; i++) {
            if (arr[i] > maxInSegment) {
                maxInSegment = arr[i];
            }
        }
        maxArr[threadCount] = maxInSegment;
        threadCount++;
    }

    public static int findMax(int[] arr) throws InterruptedException {
        int len = arr.length;
        int numThreads = 4;
        maxArr = new int[numThreads];
        MaxThread[] threads = new MaxThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new MaxThread(arr, (i * len) / numThreads, ((i + 1) * len) / numThreads);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        int globalMax = maxArr[0];
        for (int i = 1; i < numThreads; i++) {
            if (maxArr[i] > globalMax) {
                globalMax = maxArr[i];
            }
        }

        return globalMax;
    }

    public static void main(String[] args) throws InterruptedException {
        int[] arr = {1, 5, 3, 9, 2, 8, 7, 28, 4};
        int max = findMax(arr);
        System.out.println("Максимальное значение в массиве: " + max);
    }
}