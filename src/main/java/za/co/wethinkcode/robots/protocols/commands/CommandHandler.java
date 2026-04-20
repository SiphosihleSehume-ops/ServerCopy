package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.server.ClientHandler;
import za.co.wethinkcode.robots.server.World;

public abstract class CommandHandler {
      private Request request;
      private World world;

      public CommandHandler(Request request, World world) {
           this.request = request;
           this.world = world;
      }

      //Getters
      public Request getRequest() {
           return request;
      }

      public World getWorld() {
           return world;
      }

      public abstract Response execute(Request request, World world);

    public void execute(ClientHandler clientHandler) {
    }
    //Something here

}













//    private final String name;
//    private String argument;
//
//    public abstract boolean execute(Robot target);
//
//    public Command(String name){
//        this.name = name.trim().toLowerCase();
//        this.argument = "";
//    }
//
//    public Command(String name, String argument) {
//        this(name);
//        this.argument = argument.trim();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getArgument() {
//        return this.argument;
//    }
//
//    public static Command create(String instruction) {
//        if (instruction == null) throw new IllegalArgumentException("Unsupported command: null");
//        String[] args = instruction.toLowerCase().trim().split(" ");
//        String command = args[0];
//        switch (command) {
//            case "shutdown":
//            case "off":
//                return new ShutdownCommand();
//            case "help":
//                return new RobotsCommand();
//            case "forward":
//                if (args.length < 2) throw new IllegalArgumentException("Unsupported command: " + instruction);
//                return new QuitCommand(args[1]);
//            case "back":
//                if (args.length < 2) throw new IllegalArgumentException("Unsupported command: " + instruction);
//                return new DumpCommand(args[1]);
//
//            default:
//                throw new IllegalArgumentException("Unsupported command: " + instruction);
//        }
//

