# Load required libraries
library(dplyr)
library(ggplot2)

# Read the CSV file
rr_data <- read.csv("simulation_summary_Qtesting.csv", TRUE)

# Summarize performance by quantum value
summary_by_quantum <- rr_data %>%
  group_by(quantum) %>%
  summarise(
    avg_turnaround = mean(mean_turnaround),
    avg_response = mean(mean_response),
    avg_wait = mean(mean_wait),
    avg_util = mean(cpu_util),
    avg_throughput = mean(mean_throughput)
  )

print(summary_by_quantum)

# Plot to visualize how turnaround time varies with quantum
ggplot(summary_by_quantum, aes(x = quantum, y = avg_turnaround)) +
  geom_line() +
  geom_point() +
  ggtitle("Average Turnaround Time vs Quantum") +
  xlab("Quantum (ms)") +
  ylab("Average Turnaround Time (ms)")

# Find optimal quantum (based on lowest avg turnaround)
best_quantum <- summary_by_quantum %>%
  filter(avg_turnaround == min(avg_turnaround)) %>%
  select(quantum, avg_turnaround)

print(paste("Ideal quantum value (lowest avg turnaround):", best_quantum$quantum))


# Plot to visualize how turnaround time varies with quantum
ggplot(summary_by_quantum, aes(x = quantum, y = avg_wait)) +
  geom_line() +
  geom_point() +
  ggtitle("Average Wait Time vs Quantum") +
  xlab("Quantum (ms)") +
  ylab("Average Wait Time (ms)")

# Find optimal quantum (based on lowest avg wait)
best_quantumW <- summary_by_quantum %>%
  filter(avg_wait == min(avg_wait)) %>%
  select(quantum, avg_wait)

print(paste("Ideal quantum value (lowest avg wait):", best_quantumW$quantum))

# Plot to visualize how turnaround time varies with quantum
ggplot(summary_by_quantum, aes(x = quantum, y = avg_util)) +
  geom_line() +
  geom_point() +
  ggtitle("Average CPU utilization vs Quantum") +
  xlab("Quantum (ms)") +
  ylab("Average CPU util (%)")

# Find optimal quantum (based on highest CPU util)
best_quantumCPU <- summary_by_quantum %>%
  filter(avg_util == max(avg_util)) %>%
  select(quantum, avg_util)

print(paste("Ideal quantum value (Highest avg CUP util):", best_quantumCPU$quantum))