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
for SEED in 14 25 36 47 58 69 70; do
    echo "Testing for SEED=$SEED"
    
    for TIME_SLICE in $(seq 40 80); do
        LOG_FILE=output_logs/rr_quantum_tests/rr_q${TIME_SLICE}.txt

        echo "===== SEED = $SEED | TIME_SLICE = $TIME_SLICE =====" 
        java -classpath bin $MAIN_CLASS 25 2 3 $TIME_SLICE $SEED >> $LOG_FILE
        echo "" >> $LOG_FILE  # Add empty line between runs
    done
done
# -----------------------------------------------
# Loop 2: Test different CONTEXT_SWITCH values
# -----------------------------------------------
#echo "Testing different CONTEXT_SWITCH values for RR..."
#for CONTEXT_SWITCH in 1 2 3 4 5
#do
#    echo "Testing RR with CONTEXT_SWITCH=$CONTEXT_SWITCH"
#    java $MAIN_CLASS $NUM_PATRONS 2 $CONTEXT_SWITCH 3 $SEED > output_logs/rr_context_tests/rr_cs${CONTEXT_SWITCH}.txt
#done
#echo "Context switch test loop completed."

echo "Simulations completed."
