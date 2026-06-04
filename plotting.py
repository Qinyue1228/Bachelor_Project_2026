import matplotlib.pyplot as plt

sizes = [100, 200, 500, 1000, 2000]
avg_times = [237.514, 327.459, 507.433, 695.547, 739.917]
avg_times_deletion = [203.71, 506.694, 884.039, 1140.999, 1415.129]
avg_times_insertion = [396.697, 674.805, 995.633, 1172.748, 1295.993]

plt.plot(sizes, avg_times, linestyle='-', marker='o', color='green')
plt.plot(sizes, avg_times_deletion, linestyle='-', marker='o', color='blue')
plt.plot(sizes, avg_times_insertion, linestyle='-', marker='o', color='red')

plt.xlabel("The Number of Points (n)")
plt.ylabel("Average Search Time (ns)")

plt.grid(True)

plt.show()
