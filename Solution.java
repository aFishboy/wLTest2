import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {
    public static List<String> processLogs(List<String> logs, int maxSpan) {
        Map<String, Integer> signInTimes = new HashMap<>();
        Map<String, Integer> signOutTimes = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (String line : logs) {
            String[] parts = line.split(" ");
            String userId = parts[0];
            int timestamp = Integer.parseInt(parts[1]);
            String action = parts[2];

            if (action.equals("sign-in")) {
                signInTimes.put(userId, timestamp);
            } else if (action.equals("sign-out")) {
                signOutTimes.put(userId, timestamp);
            }
        }

        for (Map.Entry<String, Integer> entry : signOutTimes.entrySet()) {
            String userId = entry.getKey();
            int signOutTime = entry.getValue();

            if (signInTimes.containsKey(userId)) {
                int signInTime = signInTimes.get(userId);
                int timeSpan = signOutTime - signInTime;
                if (timeSpan <= maxSpan) {
                    result.add(userId);
                }
            }
        }
        
        Collections.sort(result, Comparator.comparingInt(Integer::parseInt));
        return result;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int logsCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> logs = IntStream.range(0, logsCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .collect(toList());

        int maxSpan = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> result = Result.processLogs(logs, maxSpan);

        bufferedWriter.write(
            result.stream()
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
