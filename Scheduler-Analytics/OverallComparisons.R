library(dplyr)
library(ggplot2)
library(janitor)

# Read and clean the CSV
data <- read.csv("simulation_summary.csv", TRUE)
data <- janitor::clean_names(data)
data <- data[, colSums(is.na(data)) < nrow(data)]

# Confirm correct structure
str(data)  # Optional: inspect the structure

# Ensure 'algorithm' is a factor
data$algorithm <- factor(data$algorithm)

non_metric_cols <- c("algorithm", "quantum", "context_switch_time", "num_patrons", "seed")
metric_cols <- setdiff(names(data), non_metric_cols)

#average across each algorithm/context switch time
summary_data <- data %>%
  group_by(algorithm, num_patrons) %>%
  summarise(across(all_of(metric_cols), mean), .groups = "drop")

#just checking
print(head(summary_data))
par(mfcol = c(2, 1))
# Plotting
metrics <- setdiff(names(summary_data), c("algorithm", "num_patrons"))

for (metric in metrics) {
  print(
    ggplot(summary_data, aes(x = num_patrons, y = .data[[metric]], color = algorithm)) +
      geom_line() +
      geom_point() +
      labs(
        title = paste("Number of patrons vs", gsub("_", " ", metric)),
        x = "Number of patrons",
        y = gsub("_", " ", metric)
      ) +
      theme_minimal()
  )
}