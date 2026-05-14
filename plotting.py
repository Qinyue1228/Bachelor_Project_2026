import matplotlib.pyplot as plt

sizes = [100, 200, 500, 1000, 2000]
avg_times = [0.04885561, 0.06882020, 0.07816515, 0.08909645, 0.10196862]

plt.plot(sizes, avg_times, linestyle='-', marker='o')

plt.xlabel("The Number of Points (n)")
plt.ylabel("Average Search Time")

plt.grid(True)

plt.show()
