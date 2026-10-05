# Parallel & Distributed Computing Lab (U21PC782CS) — Refactored Programs

Refactored, cleaned-up versions of every program in the lab record
**"PDC_LAB- Record-26-27.docx.pdf"**, organised one folder per experiment.

Each experiment folder contains:

- the runnable source file(s), and
- a `README.md` that explains the **concept**, the **APIs/classes involved**, and the
  **functionality** of the program, plus how to compile and run it.

## Index

### Part 1 — Distributed Computing (Java)

| #  | Experiment | Folder | Language |
|----|------------|--------|----------|
| 01 | Client–server communication over **TCP** | [`01-tcp-client-server/`](01-tcp-client-server/) | Java |
| 02 | Client–server communication over **UDP** | [`02-udp-client-server/`](02-udp-client-server/) | Java |
| 03 | **Chat server** (multi-client broadcast) | [`03-chat-server/`](03-chat-server/) | Java |
| 04 | **DNS using a database** (distributed name resolution) | [`04-dns-database/`](04-dns-database/) | Java + MySQL |
| 05 | **DNS using RMI** (remote method invocation) | [`05-dns-rmi/`](05-dns-rmi/) | Java |
| 06 | **Word count using Hadoop MapReduce** | [`06-hadoop-wordcount/`](06-hadoop-wordcount/) | Java + Hadoop |

### Part 2 — Parallel Programming (C/C++ and Java)

| #  | Experiment | Folder | Languages |
|----|------------|--------|-----------|
| 07 | Hello message from multiple **POSIX threads** | [`07-posix-hello-threads/`](07-posix-hello-threads/) | C + Java |
| 08 | **Merge sort** with POSIX threads | [`08-posix-merge-sort/`](08-posix-merge-sort/) | C + Java |
| 09 | **Vector addition** with OpenMP | [`09-openmp-vector-addition/`](09-openmp-vector-addition/) | C + Java |
| 10 | **Vector multiplication** with OpenMP | [`10-openmp-vector-multiplication/`](10-openmp-vector-multiplication/) | C + Java |
| 11 | **Parallel reduction** (array sum) with OpenMP | [`11-openmp-reduction/`](11-openmp-reduction/) | C + Java |

> Part 2 of the record covers **POSIX threads**, **OpenMP** and **MPI**. Every program
> that had source code in the PDF is reproduced here in its native **C** form **and**
> as a Java equivalent (`java.util.concurrent` / parallel streams) so you can compare
> the two parallel-programming models side by side. MPI appears only as theory and
> screenshots in the PDF, so no MPI source is included.

## Prerequisites

- **JDK 8+** (`javac`, `java`) for Part 1 and the Java equivalents.
- **gcc** with OpenMP (`-fopenmp`) and pthreads (`-pthread`) for the C programs.
- **MySQL/MariaDB** + the MySQL Connector/J driver for experiment 04.
- **Apache Hadoop** for experiment 06.

## How to study with this

1. Read the `README.md` in a folder first — it gives the concept and the API map.
2. Read the source top-to-bottom; the structure mirrors the exam expectations
   (identify the interface / entry point / worker classes).
3. Compile and run it (commands are in each README).
