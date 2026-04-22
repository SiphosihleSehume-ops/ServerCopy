package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.server.World;

public abstract class CommandHandler {
      private Request request;
      private World world;

      public CommandHandler(Request request, World world) {
           this.request = request;
           this.world = world;
      }

     public static Response create(Request request) {

      String instruction = request.getCommand();
      if (instruction == null) {
          return new Response("Error", World.stts());
      }
      String[] args = instruction.toLowerCase().trim().split(" ");
      String command = args[0];
      switch (command) {
          case "dump" -> new DumpCommand();
          case "quit" -> new QuitCommand();
          case "look" -> new LookCommand();
          case "robots" -> new RobotsCommand();
          case "state" -> new StateCommand();
        default -> throw new IllegalArgumentException("Unsupported command: " + command);
    }


      public abstract Response execute(Request request, World world);

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

