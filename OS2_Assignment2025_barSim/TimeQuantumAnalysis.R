#CSC3002F Assignment
#Quantum testing
q <- read.csv("simulation_summary.csv", TRUE)
#plot(q$mean_turnaround ~ q$quantum, col = c('blue','red','pink','green')[q$num_patrons], pch = 16)
q.25 = q[q$num_patrons == 25, ]
q.50 = q[q$num_patrons == 50, ]
q.75 = q[q$num_patrons == 75, ]
q.100 = q[q$num_patrons == 100, ]
par(mfcol = c(2, 2))
plot(q.25$mean_turnaround ~ q.25$quantum)
plot(q.50$mean_turnaround ~ q.50$quantum)
plot(q.75$mean_turnaround ~ q.75$quantum)
plot(q.100$mean_turnaround ~ q.100$quantum)

plot(q.25$mean_wait~ q.25$quantum)
plot(q.50$mean_wait ~ q.50$quantum)
plot(q.75$mean_wait ~ q.75$quantum)
plot(q.100$mean_wait ~ q.100$quantum)

q.25.closer = q.25[q.25$quantum > 65, ]
q.50.closer = q.50[q.50$quantum > 65, ]
q.75.closer = q.75[q.75$quantum > 65, ]
q.100.closer = q.100[q.100$quantum > 65, ]

plot(q.25.closer$mean_turnaround ~ q.25.closer$quantum)
plot(q.50.closer$mean_turnaround ~ q.50.closer$quantum)
plot(q.75.closer$mean_turnaround ~ q.75.closer$quantum)
plot(q.100.closer$mean_turnaround ~ q.100.closer$quantum)

plot(q.25$mean_wait~ q.25$quantum)
plot(q.50$mean_wait ~ q.50$quantum)
plot(q.75$mean_wait ~ q.75$quantum)
plot(q.100$mean_wait ~ q.100$quantum)

par(mfcol = c(1, 1))