# RRT_Pathfinding

A Java-based implementation of the **Rapidly-exploring Random Tree (RRT)** algorithm for visual pathfinding in a 2D space with obstacles. This project demonstrates clean object-oriented separation of logic and GUI, collision-aware random sampling, and animated environment traversal using Java's `Swing` framework. Ideal for visualizing RRT behavior and studying obstacle avoidance strategies.

---



## Project Structure

```
RRT_Pathfinding/
├── src/
│   ├── RRT_Logic.java     # Contains the core RRT algorithm and logic
│   └── RRT_GUI.java       # Handles visualization using Swing
```

---



## Core Java Classes

### `RRT_Logic.java`
Handles all logic to:
- Generate multiple 2D environments with static obstacles
- Perform random sampling and expand tree nodes using RRT
- Check for collisions with obstacles
- Backtrack to construct the shortest path when the goal is found

**Key Features:**
- Modular obstacle generation for different environments  
- Collision-aware tree construction  
- Adjustable constants for step size, environment size, and obstacle count  
- Reusable logic with public access for GUI rendering

---



### `RRT_GUI.java`
Handles:
- Graphical rendering of RRT edges, obstacles, and shortest path  
- Cycling through 3 environments using the logic defined in `RRT_Logic`  
- Color-coded visuals for path (blue), start/goal (red), and edges (light gray)

---



## How to Run

### IDE
- Open the project in an IDE like IntelliJ or Eclipse.
- Run `RRT_GUI.java` as a Java application.

### Command Line
```bash
javac RRT_Logic.java RRT_GUI.java
java RRT_GUI
```

---



## Sample Output

- Displays a centered 300x300 state space  
- Randomly generates 3 environments (some with 5 obstacles, one with 10)  
- Finds and draws the shortest path in **blue**  
- Tree nodes and edges expand randomly but avoid obstacles  
- Short pauses between each environment for visualization  

---



## Future Enhancements

- Add user interaction for placing custom obstacles and goals  
- Animate the RRT expansion in real time  
- Visualize additional metrics (e.g., number of nodes explored)  
- Export visual results as image files  

---



## Author  
**Trace Davis**  
- GitHub: [Trace0727](https://github.com/Trace0727)  
- LinkedIn: [Trace Davis](https://www.linkedin.com/in/trace-d-926380138/)
