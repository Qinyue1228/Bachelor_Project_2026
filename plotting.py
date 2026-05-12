import matplotlib.pyplot as plt

sizes = [100, 200, 500, 1000, 2000]
avg_times = [0.10586641, 0.19844785, 0.41554526, 0.81324592, 1.48341683]

plt.plot(sizes, avg_times, linestyle='-', marker='o')

plt.xlabel("The Number of Points (n)")
plt.ylabel("Average Search Time")

plt.grid(True)

plt.show()
