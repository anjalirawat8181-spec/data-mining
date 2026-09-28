import java.util.*;
public class Apriori {
    static int minSupportCount;
    static List<Set<String>> transactions = new ArrayList<>();

    static int getSupportCount(Set<String> itemset) {
        int count = 0;

        for (Set<String> transaction : transactions) {
            if (transaction.containsAll(itemset)) {
                count++;
            }
        }
        return count;
    }

    static List<Set<String>> generateCandidates(List<Set<String>> previous) {
        List<Set<String>> candidates = new ArrayList<>();
        for (int i = 0; i < previous.size(); i++) {
            for (int j = i + 1; j < previous.size(); j++) {

                Set<String> candidate = new TreeSet<>(previous.get(i));
                candidate.addAll(previous.get(j));

                if (candidate.size() == previous.get(0).size() + 1
                        && !candidates.contains(candidate)) {
                    candidates.add(candidate);
                }
            }
        }
        return candidates;
    }

    static boolean allSubsetsFrequent(Set<String> candidate,
                                      List<Set<String>> previous) {

        for (String item : candidate) {

            Set<String> subset = new TreeSet<>(candidate);
            subset.remove(item);

            if (!previous.contains(subset)) {
                return false;
            }
        }

        return true;
    }

    static List<Set<String>> apriori() {

        Map<String, Integer> itemCounts = new TreeMap<>();

        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            }
        }

        List<Set<String>> frequentItemsets = new ArrayList<>();

        for (String item : itemCounts.keySet()) {

            if (itemCounts.get(item) >= minSupportCount) {
                Set<String> itemset = new TreeSet<>();
                itemset.add(item);
                frequentItemsets.add(itemset);
            }
        }

        int k = 1;

        while (!frequentItemsets.isEmpty()) {

            System.out.println("\nFrequent " + k + "-Itemsets:");

            for (Set<String> itemset : frequentItemsets) {
                int support = getSupportCount(itemset);
                System.out.println(itemset + " -> Support Count: " + support);
            }

            List<Set<String>> candidates =
                    generateCandidates(frequentItemsets);

            List<Set<String>> nextFrequent = new ArrayList<>();

            for (Set<String> candidate : candidates) {

                if (!allSubsetsFrequent(candidate, frequentItemsets)) {
                    continue;
                }

                int support = getSupportCount(candidate);

                if (support >= minSupportCount) {
                    nextFrequent.add(candidate);
                }
            }

            frequentItemsets = nextFrequent;
            k++;
        }

        return frequentItemsets;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of transactions: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {

            System.out.print("Enter items for Transaction "
                    + (i + 1) + ": ");

            String input = sc.nextLine();

            Set<String> transaction = new TreeSet<>(
                    Arrays.asList(input.split("\\s+"))
            );

            transactions.add(transaction);
        }

        System.out.print("Enter minimum support count: ");
        minSupportCount = sc.nextInt();

        apriori();

        sc.close();
    }
}