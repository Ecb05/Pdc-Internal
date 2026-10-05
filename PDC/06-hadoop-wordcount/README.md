# Experiment 06 — Word Count using the MapReduce Model (Hadoop)

## Concept

**MapReduce** is a distributed data-processing model for counting/aggregating large
datasets across a cluster. A job has five phases:

1. **Splitting** — input is split into chunks (here, records are split into words).
2. **Mapping** — each split is processed in parallel, emitting intermediate
   `(key, value)` pairs.
3. **Intermediate splitting / shuffling** — pairs with the same key are grouped onto
   the same reducer.
4. **Reducing** — grouped values are aggregated per key.
5. **Combining** — outputs from all reducers form the final result.

A MapReduce program has three parts:

- a **Driver** (the `main` entry point that configures the `Job`),
- a **Mapper** (`extends Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`), and
- a **Reducer** (`extends Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`).

The number of occurrences of each word is the classic "Hello World" of MapReduce.

## APIs / classes involved

| API | Purpose |
|-----|---------|
| `org.apache.hadoop.conf.Configuration` | Job configuration. |
| `org.apache.hadoop.mapreduce.Job` | Represents and runs the MapReduce job. |
| `org.apache.hadoop.mapreduce.Mapper` / `Reducer` | Base classes for map/reduce logic (with inner `Context`). |
| `org.apache.hadoop.io.{LongWritable, Text, IntWritable}` | Hadoop-serializable key/value types. |
| `org.apache.hadoop.mapreduce.lib.input.FileInputFormat` | Sets the HDFS input path. |
| `org.apache.hadoop.mapreduce.lib.output.FileOutputFormat` | Sets the HDFS output path. |

## Functionality

1. **Mapper** receives each line, splits it on commas, upper-cases/trims each token and
   emits `(word, 1)`.
2. **Reducer** receives `(word, [1,1,...])` and sums the 1s, emitting `(word, count)`.
3. The **driver** wires these together, sets a combiner (a local pre-aggregation that
   cuts shuffle traffic), and waits for the job to finish.

## Files

- `WordCount.java` — driver + Mapper + Reducer.

## Build & run (needs Hadoop installed)

```bash
# 1. Compile against the Hadoop classpath:
javac -classpath "$(hadoop classpath)" WordCount.java

# 2. Package:
jar cf wc.jar WordCount*.class

# 3. Put the input into HDFS:
hadoop fs -put input.txt /user/$USER/input.txt

# 4. Run:
hadoop jar wc.jar WordCount /user/$USER/input.txt /user/$USER/output

# 5. View the result:
hadoop fs -cat /user/$USER/output/part-r-00000
```

Sample output:

```
BUS   7
CAR   4
TRAIN 6
```

## Notes / exam tips

- The `KEYIN`/`VALUEIN` of the mapper are `LongWritable` (byte offset) and `Text`
  (the line).
- A **combiner** is optional but is a standard optimisation and is often expected in
  an exam answer.
- The output path must **not already exist**, or Hadoop fails the job.
- The commented original used a comma split; change `split(",")` to `split("\\s+")`
  to split on whitespace instead.
