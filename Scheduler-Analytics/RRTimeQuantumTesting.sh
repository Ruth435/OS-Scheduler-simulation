#!/bin/bash

# Exit on error
set -e

# Compile Java source files
echo "Compiling Java files..."
make all

MAIN_CLASS="barScheduling.SchedulingSimulation"


# Output directory
mkdir -p output_logs/rr_context_tests

# -----------------------------------------------
# Loop 1: Test different TIME_SLICES (quantums) with different seeds
# -----------------------------------------------
for NUM_PATRONS in 25 50 75 100
do
    for SEED in 14 25 36 47 58 69 70; do
        echo "Testing for SEED=$SEED"
    
        for TIME_SLICE in $(seq 30 100); do
            LOG_FILE=output_logs/rr_quantum_tests/rr_q${TIME_SLICE}.txt

            echo "===== SEED = $SEED | TIME_SLICE = $TIME_SLICE =====" 
            java -classpath bin $MAIN_CLASS $NUM_PATRONS 2 3 $TIME_SLICE $SEED >> $LOG_FILE
            echo "" >> $LOG_FILE  # Add empty line between runs
        done
    done
done

echo "Simulations completed."
