#include <stdlib.h>
#include <connectionHandler.h>
#include <boost/thread.hpp>

#include "../include/connectionHandler.h"

std::atomic<bool> isLoggedIn(true);

class InputTask
{
private:

    ConnectionHandler * myHandler;

public:

    InputTask (ConnectionHandler * handler) :myHandler(handler) {}


    void run ()
    {
        while (isLoggedIn) {
            std::string answer;
            if (!myHandler->getLine(answer)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }

            int len = answer.length();
            // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
            // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
            answer.resize(len - 1);
            std::cout << answer << std::endl;
            if (answer == "ACK signout succeeded") {
                std::cout << "Ready to exit. Please press enter" << std::endl << std::endl;
                isLoggedIn = false;
                break;
            }
        }
    }
};


class OutputTask {

private:

    ConnectionHandler * myHandler;

public:

    OutputTask(ConnectionHandler * handler) : myHandler(handler) {}

    void run()
    {
        while (isLoggedIn) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            if (!myHandler->sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }

        }
    }
};


int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    OutputTask output(&connectionHandler);
    InputTask input(&connectionHandler);
    boost:: thread outputThread(&OutputTask::run,&output);
    boost:: thread inputThread(&InputTask::run,&input);
    outputThread.join();
    inputThread.join();
    return 0;
}
