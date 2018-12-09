import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day08 {
    private static Path input;
    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day08").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String s = Files.readAllLines(input).get(0);
        List<Integer> tokens = Arrays.stream(s.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        TreeNode tree = parseTree(tokens);

        System.out.println(part1(tree));
        System.out.println(part2(tree));
    }

    private static int part1(TreeNode node){
        int currentSum = node.getMetadata().stream().mapToInt(Integer::intValue).sum();
        if(node.getChildren().isEmpty()){
            return currentSum;
        } else {
            return currentSum + node.getChildren().stream().mapToInt(Day08::part1).sum();
        }
    }

    private static int part2(TreeNode node){
        if(node.getChildren().isEmpty()){
            return node.getMetadata().stream().mapToInt(Integer::intValue).sum();
        } else {
            int sum = 0;
            for (int metadatum : node.getMetadata()) {
                if(metadatum <= node.getChildren().size()){
                    sum += part2(node.getChildren().get(metadatum - 1));
                }
            }
            return sum;
        }
    }

    private static TreeNode parseTree(List<Integer> tokens) {
        int tokenIndex = 0;
        TreeNode root = new TreeNode(tokens.get(tokenIndex++), tokens.get(tokenIndex++));

        TreeNode currentNode = root;
        while(tokenIndex < tokens.size()) {
            if(currentNode.getChildCount() == currentNode.getChildren().size()) {
                // set metadata
                for(int i = 0; i < currentNode.getMetadataCount(); i++){
                    currentNode.addMetadata(tokens.get(tokenIndex++));
                }
                // set currentNode to Parent
                currentNode = currentNode.getParent();
            }

            else {
                // add new child node
                TreeNode newNode = new TreeNode(tokens.get(tokenIndex++), tokens.get(tokenIndex++));
                currentNode.addChild(newNode);
                newNode.setParent(currentNode);
                currentNode = newNode;
            }
        }

        return root;
    }
}

class TreeNode {
    private TreeNode parent;
    private List<TreeNode> children;
    private final int childCount;
    private List<Integer> metadata;
    private final int metadataCount;

    TreeNode(int childCount, int metadataCount) {
        children = new ArrayList<>();
        metadata = new ArrayList<>();
        this.childCount = childCount;
        this.metadataCount = metadataCount;
    }

    void addChild(TreeNode node) {
        this.children.add(node);
    }

    void addMetadata(int metadata) {
        this.metadata.add(metadata);
    }

    int getMetadataCount() {
        return metadataCount;
    }

    TreeNode getParent() {
        return parent;
    }

    void setParent(TreeNode parent) {
        this.parent = parent;
    }

    int getChildCount() {
        return childCount;
    }

    List<TreeNode> getChildren() {
        return children;
    }

    List<Integer> getMetadata() {
        return metadata;
    }
}