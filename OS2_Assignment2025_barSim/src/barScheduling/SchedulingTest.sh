# Exit immediately if any command fails
set -e

# Compile all Java source files
echo "Compiling Java files..."
javac *.java

MAIN_CLASS="SchedulingSimulation.java"

#common parameters
NUM_PATRONS=5
CONTEXT_SWITCH=10
TIME_SLICE=3
SEED=42

# Create output directory if it doesn't exist
mkdir -p output_logs

# Test FCSF (0)
echo "Running FCSF scheduling..."
java $MAIN_CLASS $NUM_PATRONS 0 $CONTEXT_SWITCH 0 $SEED > output_logs/fcsf_output.txt
echo "FCSF test completed."

# Test SJF (1)
echo "Running SJF scheduling..."
java $MAIN_CLASS $NUM_PATRONS 1 $CONTEXT_SWITCH 0 $SEED > output_logs/sjf_output.txt
echo "SJF test completed."

# Test RR (2)
echo "Running RR scheduling..."
java $MAIN_CLASS $NUM_PATRONS 2 $CONTEXT_SWITCH $TIME_SLICE $SEED > output_logs/rr_output.txt
echo "RR test completed."

echo "✅ All scheduling algorithm tests completed. Check the output_logs/ directory."
