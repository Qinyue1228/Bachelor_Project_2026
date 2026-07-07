import matplotlib.pyplot as plt
import math

sizes = [100, 200, 500, 1000, 2000]
avg_times = [256.032, 338.67, 529.539, 598.587, 730.651]
avg_times_deletion = [148.912, 264.798, 625.014, 870.548, 1201.97]
avg_times_insertion = [325.275, 490.391, 768.604, 968.194, 1052.899]

# plt.plot(sizes, avg_times, linestyle='-', marker='o', color='green', label="Range query")
plt.plot(sizes, avg_times_deletion, linestyle='-', marker='o', color='blue', label="Deletion")
plt.plot(sizes, avg_times_insertion, linestyle='-', marker='o', color='red', label="Insertion")

plt.xlabel("The Number of Points (n)")
plt.ylabel("Running Time (ns)")

plt.grid(True)

plt.legend()
plt.show()
