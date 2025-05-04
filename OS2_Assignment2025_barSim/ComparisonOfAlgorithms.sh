#!/bin/bash

# Exit on error
set -e

# Compile Java source files
echo "Compiling Java files..."
make all

MAIN_CLASS="barScheduling.SchedulingSimulation"


# Output directory
mkdir -p output_logs/comparison_tests

# -----------------------------------------------
# Loop 1: compare different algorithms with different seeds and number of patrons
# -----------------------------------------------
for x in 1 2 3 4; do
for NUM_PATRONS in 5 10 25 50 75; do
    for SEED in 14 25 36 47 58 69 70; do
        echo "Testing for SEED=$SEED"
        LOG_FILE=output_logs/comparison_tests/comparingAll3${SEED}.txt
        echo "===== SEED = $SEED | ALGORITHM = RR =====" 
        java -classpath bin $MAIN_CLASS $NUM_PATRONS 2 3 80 $SEED >> $LOG_FILE
        echo "===== SEED = $SEED | ALGORITHM = FCFS =====" 
        java -classpath bin $MAIN_CLASS $NUM_PATRONS 0 3 0 $SEED >> $LOG_FILE
        echo "===== SEED = $SEED | ALGORITHM = SJF =====" 
        java -classpath bin $MAIN_CLASS $NUM_PATRONS 1 3 0 $SEED >> $LOG_FILE
        echo "" >> $LOG_FILE  # Add empty line between runs
    done
done
done

echo "Simulations completed."