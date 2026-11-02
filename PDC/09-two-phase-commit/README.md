# Experiment — Two-Phase Commit (2PC)

## TL;DR: How the application works

The coordinator manages one transaction across multiple participants using two phases:

1. **Prepare phase:** The coordinator sends `PREPARE` to every participant and collects their `YES` or `NO` votes.
2. **Commit phase:** If every participant votes `YES`, the coordinator sends `COMMIT`. If any participant votes `NO` or does not respond, the coordinator sends `ABORT`.

## Purpose of each file

- **Coordinator.java** — Starts the server on port 5000, accepts the required number of participants, sends the transaction's `PREPARE` message, collects votes, and broadcasts the final `COMMIT` or `ABORT` decision.
- **Participant.java** — Connects to the coordinator, receives the `PREPARE` message, asks the user to vote `YES` or `NO`, sends the vote, and follows the final decision.

## Basic communication flow

1. Start the coordinator and enter the number of participants.
2. Start that many participant programs in separate terminals.
3. The coordinator accepts all participant connections.
4. Enter the transaction details at the coordinator.
5. The coordinator sends `PREPARE` to every participant.
6. Each participant sends a `YES` or `NO` vote.
7. If all votes are `YES`, the coordinator sends `COMMIT`; otherwise, it sends `ABORT`.
8. Every participant receives the decision and exits.

## Compile

Run this command from this folder:

```bash
javac Coordinator.java Participant.java
```

## Run

Open separate terminals.

**Terminal 1 — Start the coordinator:**

```bash
java Coordinator
```

Enter the number of participants and wait for them to connect.

**Terminal 2 and additional terminals — Start participants:**

```bash
java Participant
```

After all participants connect, enter the transaction details in the coordinator terminal. Each participant must enter `YES` or `NO` when prompted.

## Main concepts

- **ServerSocket:** Waits for participant connections.
- **Socket:** Represents a TCP connection between the coordinator and a participant.
- **BufferedReader:** Receives messages and votes.
- **PrintWriter:** Sends messages and votes.
- **PREPARE:** Asks participants whether they are ready to commit.
- **COMMIT:** Confirms the transaction when all participants vote YES.
- **ABORT:** Cancels the transaction when any participant votes NO.
