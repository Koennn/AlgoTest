import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class BPPTest {

    public static final int BOX_SIZE = 10;
    public static final int LOAD_COUNT = 5;
    public static final int AMOUNT = 100000;

    public static final Random RANDOM = ThreadLocalRandom.current();

    public static void main(String[] args) {
        Integer[][] samples = new Integer[AMOUNT][LOAD_COUNT];
        for (int i = 0; i < AMOUNT; i++) {
            samples[i] = generateLoad();
        }

        long time = System.currentTimeMillis();
        testAlgo(0, samples);
        System.out.println("time taken: " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        testAlgo(1, samples);
        System.out.println("time taken: " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        testAlgo(2, samples);
        System.out.println("time taken: " + (System.currentTimeMillis() - time) + "ms");
    }

    public static void testAlgo(int algo, Integer[][] samples) {
        int[] avg = new int[AMOUNT];
        int[] wasted = new int[AMOUNT];

        for (int i = 0; i < AMOUNT; i++) {
            int[] result = algo == 0 ? bestFit(samples[i]) : algo == 1 ? firstFit(samples[i]) : custom(samples[i]);
            avg[i] = result[0];
            wasted[i] = result[1];
        }

        int aw = 0;
        int lw = Integer.MAX_VALUE;
        int hw = 0;
        for (int w : wasted) {
            if (w < lw) {
                lw = w;
            }
            if (w > hw) {
                hw = w;
            }
            aw += w;
        }

        aw /= (double) wasted.length;
        int averageWasted = (int) Math.round((aw / (double) BOX_SIZE) * 100.0);
        int lowestWasted = (int) Math.round((lw / (double) BOX_SIZE) * 100.0);
        int highestWasted = (int) Math.round((hw / (double) BOX_SIZE) * 100.0);

        int average = 0;
        int lowest = Integer.MAX_VALUE;
        int highest = 0;
        for (int n : avg) {
            if (n < lowest) {
                lowest = n;
            }
            if (n > highest) {
                highest = n;
            }
            average += n;
        }

        System.out.println();
        System.out.println(algo == 0 ? "Best Fit Decreasing" : algo == 1 ? "First Fit" : "Custom");
        System.out.println("average: " + average / AMOUNT);
        System.out.println("lowest: " + lowest);
        System.out.println("highest: " + highest);
        System.out.println("box usage: " + Math.round(((average / (double) AMOUNT) / (double) LOAD_COUNT) * 100.0) + "%");
        System.out.println("lowest wasted: " + lowestWasted + "%");
        System.out.println("highest wasted: " + highestWasted + "%");
        System.out.println("average wasted: " + averageWasted + "%");
    }

    public static int[] bestFit(Integer[] load) {
        List<Integer> items = new ArrayList<>();
        Collections.addAll(items, load);
        items.sort((i1, i2) -> i1 < i2 ? 1 : i1.equals(i2) ? 0 : -1);

        List<List<Integer>> bins = new ArrayList<>();
        bins.add(new ArrayList<>());

        for (Integer item : items) {
            int lastEmpty = Integer.MAX_VALUE;
            int index = -1;
            for (List<Integer> bin : bins) {
                int empty = BOX_SIZE - (currentBinLoad(bin) + item);
                if (empty >= 0 && empty < lastEmpty) {
                    lastEmpty = empty;
                    index = bins.indexOf(bin);
                }
            }
            if (index >= 0) {
                bins.get(index).add(item);
            } else {
                List<Integer> bin = new ArrayList<>();
                bin.add(item);
                bins.add(bin);
            }
        }

        int[] avg = new int[bins.size()];
        for (List<Integer> bin : bins) {
            int usage = 0;
            for (Integer item : bin) {
                usage += item;
            }
            avg[bins.indexOf(bin)] = BOX_SIZE - usage;
        }

        int average = 0;
        for (int n : avg) {
            average += n;
        }

        return new int[]{bins.size(), (int) Math.round(average / (double) bins.size())};
    }

    public static int[] firstFit(Integer[] load) {
        List<Integer> items = new ArrayList<>();
        Collections.addAll(items, load);

        List<List<Integer>> bins = new ArrayList<>();
        bins.add(new ArrayList<>());

        for (Integer item : items) {
            int index = -1;
            for (List<Integer> bin : bins) {
                if ((currentBinLoad(bin) + item) <= BOX_SIZE) {
                    index = bins.indexOf(bin);
                    break;
                }
            }
            if (index >= 0) {
                bins.get(index).add(item);
            } else {
                List<Integer> bin = new ArrayList<>();
                bin.add(item);
                bins.add(bin);
            }
        }

        int[] avg = new int[bins.size()];
        for (List<Integer> bin : bins) {
            int usage = 0;
            for (Integer item : bin) {
                usage += item;
            }
            avg[bins.indexOf(bin)] = BOX_SIZE - usage;
        }

        int average = 0;
        for (int n : avg) {
            average += n;
        }

        return new int[]{bins.size(), (int) Math.round(average / (double) bins.size())};
    }

    public static int[] custom(Integer[] load) {
        List<Integer> items = new ArrayList<>();
        Collections.addAll(items, load);
        Collections.shuffle(items, RANDOM);

        List<List<Integer>> bins = new ArrayList<>();
        bins.add(new ArrayList<>());

        for (Integer item : items) {
            int index = -1;
            Collections.shuffle(bins, RANDOM);
            for (List<Integer> bin : bins) {
                if ((currentBinLoad(bin) + item) <= BOX_SIZE) {
                    index = bins.indexOf(bin);
                    break;
                }
            }
            if (index >= 0) {
                bins.get(index).add(item);
            } else {
                List<Integer> bin = new ArrayList<>();
                bin.add(item);
                bins.add(bin);
            }
        }

        int[] avg = new int[bins.size()];
        for (List<Integer> bin : bins) {
            int usage = 0;
            for (Integer item : bin) {
                usage += item;
            }
            avg[bins.indexOf(bin)] = BOX_SIZE - usage;
        }

        int average = 0;
        for (int n : avg) {
            average += n;
        }

        return new int[]{bins.size(), (int) Math.round(average / (double) bins.size())};
    }

    public static int currentBinLoad(List<Integer> bin) {
        int load = 0;
        for (int item : bin) {
            load += item;
        }
        return load;
    }

    public static Integer[] generateLoad() {
        Random random = ThreadLocalRandom.current();
        Integer[] load = new Integer[LOAD_COUNT];
        for (int i = 0; i < load.length; i++) {
            load[i] = random.nextInt(BOX_SIZE) + 1;
        }
        return load;
    }
}
