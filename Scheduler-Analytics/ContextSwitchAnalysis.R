library(dplyr)
library(ggplot2)
library(janitor)

# Read and clean the CSV
data <- read.csv("simulation_summary_ContextSwitchTesting.csv", TRUE)
data <- janitor::clean_names(data)
data <- data[, colSums(is.na(data)) < nrow(data)]
data <- filter(data, num_patrons == 75)

# Confirm correct structure
str(data)  # Optional: inspect the structure

# Ensure 'algorithm' is a factor
data$algorithm <- factor(data$algorithm)

non_metric_cols <- c("algorithm", "quantum", "context_switch_time", "num_patrons", "seed")
metric_cols <- setdiff(names(data), non_metric_cols)

#average across each algorithm/context switch time
summary_data <- data %>%
  group_by(algorithm, context_switch_time) %>%
  summarise(across(all_of(metric_cols), mean), .groups = "drop")

#just checking
print(head(summary_data))
par(mfcol = c(2, 1))
# Plotting
metrics <- setdiff(names(summary_data), c("algorithm", "context_switch_time"))

for (metric in metrics) {
  print(
    ggplot(summary_data, aes(x = context_switch_time, y = .data[[metric]], color = algorithm)) +
      geom_line() +
      geom_point() +
      labs(
        title = paste("Context Switch Time vs", gsub("_", " ", metric)),
        x = "Context Switch Time (ms)",
        y = gsub("_", " ", metric)
      ) +
      theme_minimal()
  )
}