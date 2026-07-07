#!/bin/bash

# Exit on error
set -e

# Compile Java source files
echo "Compiling Java files..."
make all

MAIN_CLASS="barScheduling.SchedulingSimulation"


# Output directory
mkdir -p output_logs/context_tests

# -----------------------------------------------
# Loop 1: Test different CONTEXT_SWITCH times with different seeds
# -----------------------------------------------
for NUM_PATRONS in 25 50 75 100; do
    for SEED in 14 25 36 47 58 69 70; do
        echo "Testing for SEED=$SEED"
        for CONTEXT_SWITCH in 1 2 3 4 5; do
            LOG_FILE=output_logs/context_tests/CS${CONTEXT_SWITCH}.txt

            echo "===== SEED = $SEED | CONTEXT_SWITCH = $CONTEXT_SWITCH | ALGORITHM = RR =====" 
            java -classpath bin $MAIN_CLASS $NUM_PATRONS 2 $CONTEXT_SWITCH 81 $SEED >> $LOG_FILE
            echo "===== SEED = $SEED | CONTEXT_SWITCH = $CONTEXT_SWITCH | ALGORITHM = FCFS =====" 
            java -classpath bin $MAIN_CLASS $NUM_PATRONS 0 $CONTEXT_SWITCH 0 $SEED >> $LOG_FILE
            echo "===== SEED = $SEED | CONTEXT_SWITCH = $CONTEXT_SWITCH | ALGORITHM = SJF =====" 
            java -classpath bin $MAIN_CLASS $NUM_PATRONS 1 $CONTEXT_SWITCH 0 $SEED >> $LOG_FILE
            echo "" >> $LOG_FILE  # Add empty line between runs
        done
    done
done

echo "Simulations completed."
