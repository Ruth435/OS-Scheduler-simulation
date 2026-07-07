# OS Process Scheduling Simulator: Sarah the Zombie Barman

## Project Overview
This repository contains an advanced CPU process scheduling simulator based on a legacy academic framework provided by the University of Cape Town (CSC3002F). 

The simulation models a highly concurrent resource allocation problem using a creative analogy:
* **CPU / Scheduler** = Sarah the Zombie Barman
* **Processes** = Patrons arriving dynamically throughout the evening
* **Burst Time / Jobs** = Orders of 1 to 5 drinks that must be prepared and served

The objective of the project was to implement, profile, and evaluate three core scheduling paradigms under variable workload stresses, backing up theoretical claims with experimental data.

---

## Academic Disclaimer
The architectural baseline of this simulation package was designed by the Department of Computer Science at the University of Cape Town. All individual algorithm profiling, debugging implementations, metric analysis, and comparative benchmarking workflows were completed independently by me.

---

## Algorithms Evaluated
1. **First-Come, First-Served (FCFS):** A non-preemptive scheduling scheme where patrons are served strictly in order of arrival.
2. **Shortest Job First (SJF):** A non-preemptive algorithm selecting the order with the lowest total drink count to optimize wait times.
3. **Round Robin (RR):** A preemptive scheme utilizing cyclical time slicing (time quanta) to switch between pending patron orders fairly.

---

## Statistical Analysis & Performance Metrics
Leveraging a background in **Applied Statistics**, the core focus of this project was to benchmark performance metrics across all three algorithms to analyze operational efficiency and user fairness:

* **Turnaround Time (TAT):** Total time elapsed from a patron's arrival to their final drink being served.
* **Waiting Time:** The cumulative duration a patron spent waiting in the queue.
* **Response Time:** The duration from arrival to the first drink being initiated.
* **System Predictability & Fairness:** Analyzed variance and distribution profiles of wait times to identify starvation patterns and quantum boundary behaviors in Round Robin.

---

## Constraints & Engineering Integrity
* **Legacy Constraints:** Adhered strictly to core systemic boundaries—the original command-line arguments, foundational simulation engines, arrival distributions, and fixed job workloads were preserved while refactoring code optimization paths.
* **Version Control:** Includes the minimal historical `git log` used to track major milestones and bug fixes.

---
