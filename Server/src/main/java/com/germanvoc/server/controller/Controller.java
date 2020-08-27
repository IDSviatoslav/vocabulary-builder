package com.germanvoc.server.controller;

import com.germanvoc.server.model.User;
import com.germanvoc.server.model.Word;
import com.germanvoc.server.repository.UserDatabase;
import com.germanvoc.server.repository.WordDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {
    @Autowired
    UserDatabase userDb;
    @Autowired
    WordDatabase wordDb;

    List<Word> quizWords;
    List<String> userAnswers;
    int numOfAnswer;

    @GetMapping("/show_all")
    Iterable<User> showAll(){
        return userDb.findAll();
    }

    @PostMapping ("/get_quiz")
    List<Word> getQuiz(@RequestParam int handle, @RequestParam int wordNum){
        quizWords = new ArrayList<>();
        userAnswers = new ArrayList<>();
        numOfAnswer = 0;
        int count = 0;
        Random r = new Random();
        if (userDb.findFirstByHandle(handle) != null) {
            while (count < wordNum) {
                Optional<Word> optionalWord = wordDb.findById(r.nextInt(11));
                if (optionalWord.isPresent() && !quizWords.contains(optionalWord.get())){
                    quizWords.add(optionalWord.get());
                    count++;
                }
            }
        }
        System.out.println("QuizWords sent with size = " + quizWords.size());
        System.out.println("count = " + quizWords.size());
        for (Word el : quizWords) System.out.println(el.toString());
        System.out.println("______________________________");
        return quizWords;
    }

    @PostMapping ("/check_answer")
    Map checkAnswer(@RequestParam int handle, @RequestParam String userAnswer){
        System.out.println(handle + ": answer" + userAnswer);
        if (userDb.findFirstByHandle(handle) != null && numOfAnswer < quizWords.size()) {
            userAnswers.add(userAnswer);
            Word rightAnswer = quizWords.get(numOfAnswer);
            if (rightAnswer.getOriginal().equals(userAnswer)){
                numOfAnswer++;
                System.out.println(Integer.toString(numOfAnswer) + ": right");
                return Collections.singletonMap("answerStatus","right");
            }
        }
        numOfAnswer++;
        System.out.println(Integer.toString(numOfAnswer) + ": wrong");
        return Collections.singletonMap("answerStatus","wrong");
    }

    @PostMapping ("/getQuizResult")
    Map quizResult(@RequestParam int handle){
        HashMap quizResult = new HashMap();
        if (userDb.findFirstByHandle(handle) != null) {
            for (int i = 0; i < userAnswers.size(); i++){
                quizResult.put(quizWords.get(i).getOriginal(), userAnswers.get(i));
            }
        }
        System.out.println("QuizResult sent with size = " + quizResult.size());
        return quizResult;
    }

    @PostMapping(path = "/add_word")
    String addWord(@RequestBody Word newWord){
        wordDb.save(newWord);
        return newWord.toString();
    }

    @PostMapping(path = "/create_account")
    Map createAccount(@RequestBody User newUser){
        String resp;
        if (userDb.findFirstByName(newUser.getName()) == null && newUser.getName()!="" && newUser.getPassword()!="") {
            resp = "user created";
            //newUser.setHandle(generateHandle(newUser));
            userDb.save(newUser);
        }
        else resp = "user with this name exists:";
        return Collections.singletonMap("accountInfo", resp);
    }

    @PostMapping(path = "/login")
    Map login(@RequestBody User potentialUser){
        User userMatches = userDb.findFirstByName(potentialUser.getName());
        if(userMatches!=null){
            System.out.println("base user:" + userMatches.getPassword());
            System.out.println("pot user:" + potentialUser.getPassword());
            userMatches.setHandle(generateHandle(userMatches));
            userDb.save(userMatches);
            System.out.println(Collections.singletonMap("handle", userMatches.getHandle()).toString());
            if (userMatches.getPassword().equals(potentialUser.getPassword())) return Collections.singletonMap("handle", userMatches.getHandle());
        }
        System.out.println(Collections.singletonMap("accountInfo", "invalid auth"));

        return Collections.singletonMap("accountInfo", "invalid auth");
    }

    int generateHandle(User user){
        int res;
        res = generatePrime()*user.getName().hashCode();
        res += generatePrime()*user.getPassword().hashCode();
        return res;
    }

    int generatePrime(){
        int[] primes = {13, 17, 23, 29, 31};
        Random r = new Random();
        return primes[r.nextInt(5)];
    }
}